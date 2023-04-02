package ml.bigbrains.gitiki.model;

import java.util.List;

public record Request(List<String> path, String name) {
}
