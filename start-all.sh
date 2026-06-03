#!/bin/bash
#
# 启动所有服务模块（除 cloud-sample-api 外）
# 使用方式: ./start-all.sh
# 停止所有服务: ./start-all.sh stop
#

set -e

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_DIR="$BASE_DIR/logs"
PID_DIR="$BASE_DIR/.pids"

mkdir -p "$LOG_DIR" "$PID_DIR"

# 模块列表: 目录名 | 显示名称 | 端口
MODULES=(
  "cloud-provider-dubbo-sample|provider-dubbo|-"
  "cloud-provider-reactive-sample|provider-reactive|8762"
  "cloud-consumer-reactive-sample|consumer-reactive|8763"
  "cloud-gateway-sample|gateway|8764"
  "cloud-provider-sample|provider|8765"
  "cloud-consumer-sample|consumer|8766"
)

start_module() {
  local module_dir="$1"
  local display_name="$2"
  local port="$3"
  local pid_file="$PID_DIR/$display_name.pid"
  local log_file="$LOG_DIR/$display_name.log"

  # 检查是否已在运行（通过 PID 文件）
  if [ -f "$pid_file" ] && kill -0 "$(cat "$pid_file")" 2>/dev/null; then
    echo "[$display_name] 已在运行 (PID: $(cat "$pid_file"))"
    return
  fi

  # 检查端口是否已被占用（服务可能通过其他方式启动）
  if [ "$port" != "-" ]; then
    if curl -s -o /dev/null --connect-timeout 2 "http://localhost:$port" 2>/dev/null; then
      echo "[$display_name] 端口 $port 已被占用，跳过启动"
      return
    fi
  else
    # Dubbo 模块检查 50051 端口
    if lsof -i :50051 >/dev/null 2>&1; then
      echo "[$display_name] 端口 50051 已被占用，跳过启动"
      return
    fi
  fi

  echo -n "[$display_name] 启动中 (port: $port) ..."
  cd "$BASE_DIR"
  nohup ./mvnw -pl "$module_dir" spring-boot:run > "$log_file" 2>&1 &
  local pid=$!
  echo "$pid" > "$pid_file"

  # 等待启动（最多 60 秒，mvn spring-boot:run 比 java -jar 慢一些）
  for i in $(seq 1 60); do
    if ! kill -0 "$pid" 2>/dev/null; then
      echo " 失败! 请查看日志: $log_file"
      rm -f "$pid_file"
      return 1
    fi
    if [ "$port" = "-" ]; then
      # 无 HTTP 端口的模块，检测进程是否存活
      if kill -0 "$pid" 2>/dev/null; then
        echo " 已启动 (PID: $pid)"
        return 0
      fi
    elif curl -s -o /dev/null -w '' "http://localhost:$port" 2>/dev/null; then
      echo " 成功 (PID: $pid, port: $port)"
      return 0
    fi
    sleep 1
  done

  echo " 超时! 请查看日志: $log_file"
  rm -f "$pid_file"
  return 1
}

stop_all() {
  echo "正在停止所有服务..."
  for pid_file in "$PID_DIR"/*.pid; do
    [ -f "$pid_file" ] || continue
    local name=$(basename "$pid_file" .pid)
    local pid=$(cat "$pid_file")
    if kill -0 "$pid" 2>/dev/null; then
      echo -n "[$name] 停止中 (PID: $pid) ..."
      kill "$pid"
      # 等待进程退出（最多 10 秒）
      for i in $(seq 1 10); do
        if ! kill -0 "$pid" 2>/dev/null; then
          break
        fi
        sleep 1
      done
      if kill -0 "$pid" 2>/dev/null; then
        kill -9 "$pid" 2>/dev/null
      fi
      echo " 已停止"
    else
      echo "[$name] 未在运行"
    fi
    rm -f "$pid_file"
  done
  rm -rf "$LOG_DIR" "$PID_DIR"
  echo "所有服务已停止，logs 和 .pids 目录已清理"
}

# README 中的演示 URL 列表
demo_urls() {
  echo "========== 访问演示 URL =========="
  local urls=(
    "http://localhost:8763/hi?name=hongxi|直接访问 consumer-reactive"
    "http://localhost:8764/consumer-reactive-sample/hi?name=hongxi|通过网关访问 consumer-reactive"
    "http://localhost:8766/hi?name=hongxi|直接访问 consumer"
    "http://localhost:8764/consumer-sample/hi?name=hongxi|通过网关访问 consumer"
    "http://localhost:8766/dubbo?name=hongxi|直接访问 consumer (dubbo)"
    "http://localhost:8764/consumer-sample/dubbo?name=hongxi|通过网关访问 consumer (dubbo)"
    "http://localhost:8763/dubbo?name=hongxi|直接访问 consumer-reactive (dubbo)"
    "http://localhost:8764/consumer-reactive-sample/dubbo?name=hongxi|通过网关访问 consumer-reactive (dubbo)"
  )
  for entry in "${urls[@]}"; do
    IFS='|' read -r url desc <<< "$entry"
    echo ""
    echo "[$desc]"
    echo "  URL: $url"
    local response
    response=$(curl -s -w '\n  HTTP Status: %{http_code}' "$url" 2>/dev/null)
    if [ $? -eq 0 ]; then
      echo "  响应: $response"
    else
      echo "  请求失败"
    fi
  done
  echo ""
  echo "=================================="
}

status_all() {
  echo "========== 服务状态 =========="
  printf "%-22s %-8s %s\n" "模块" "状态" "PID"
  printf "%-22s %-8s %s\n" "----" "----" "---"
  for entry in "${MODULES[@]}"; do
    IFS='|' read -r module_dir display_name port <<< "$entry"
    local pid_file="$PID_DIR/$display_name.pid"
    if [ -f "$pid_file" ] && kill -0 "$(cat "$pid_file")" 2>/dev/null; then
      printf "%-22s %-8s %s\n" "$display_name" "运行中" "$(cat "$pid_file")"
    else
      printf "%-22s %-8s %s\n" "$display_name" "已停止" "-"
      rm -f "$pid_file"
    fi
  done
  echo "=============================="
}

# Nacos 注册中心地址
NACOS_HOST="127.0.0.1"
NACOS_PORT="8848"

check_nacos() {
  echo -n "[Nacos] 检查注册中心 ($NACOS_HOST:$NACOS_PORT) ..."
  for i in $(seq 1 15); do
    if curl -s -o /dev/null -w '' "http://$NACOS_HOST:$NACOS_PORT/nacos/actuator/health" 2>/dev/null; then
      echo " 就绪"
      return 0
    fi
    sleep 2
  done
  echo " 未就绪!"
  echo "请先启动 Nacos (http://$NACOS_HOST:$NACOS_PORT/nacos)，再运行本脚本。"
  exit 1
}

install_api() {
  echo "正在安装 cloud-sample-api 到本地仓库 ..."
  cd "$BASE_DIR"
  # 先安装根 pom，再安装 api 模块（api 依赖父 pom）
  if ./mvnw -N install -q && ./mvnw -pl cloud-sample-api install -DskipTests -q; then
    echo "cloud-sample-api 安装成功"
  else
    echo "cloud-sample-api 安装失败!"
    exit 1
  fi
}

# 主逻辑
case "${1:-start}" in
  start)
    echo "========== 启动所有服务 =========="
    check_nacos
    echo ""
    install_api
    echo ""
    for entry in "${MODULES[@]}"; do
      IFS='|' read -r module_dir display_name port <<< "$entry"
      start_module "$module_dir" "$display_name" "$port"
    done
    echo ""
    status_all
    demo_urls
    ;;
  stop)
    stop_all
    ;;
  restart)
    stop_all
    sleep 2
    echo ""
    echo "========== 重新启动所有服务 =========="
    check_nacos
    echo ""
    install_api
    echo ""
    for entry in "${MODULES[@]}"; do
      IFS='|' read -r module_dir display_name port <<< "$entry"
      start_module "$module_dir" "$display_name" "$port"
    done
    echo ""
    status_all
    demo_urls
    ;;
  status)
    status_all
    ;;
  *)
    echo "用法: $0 {start|stop|restart|status}"
    exit 1
    ;;
esac
