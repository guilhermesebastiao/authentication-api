package dev.guilhermesebastiao.authenticationapi.application.command;

public record PageQueryCommand(
        int page,
        int size
) {
    public PageQueryCommand {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be greater than zero");
        }

        if (size < 1) {
            throw new IllegalArgumentException("Size must be greater than zero");
        }
    }

    public int zeroBasedPage() {
        return page - 1;
    }

    public long offset() {
        return (long) (page - 1) * size;
    }
}
