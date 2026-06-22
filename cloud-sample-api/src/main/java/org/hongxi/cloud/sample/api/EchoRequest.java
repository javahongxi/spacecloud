package org.hongxi.cloud.sample.api;

import java.io.Serial;
import java.io.Serializable;

public record EchoRequest(String message) implements Serializable {
    @Serial
    private static final long serialVersionUID = -2080533871181613309L;
}
