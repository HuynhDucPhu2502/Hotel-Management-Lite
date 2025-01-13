package iuh.fit.models.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ObjectStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String displayname;

    @Override
    public String toString() {
        return displayname;
    }
}
