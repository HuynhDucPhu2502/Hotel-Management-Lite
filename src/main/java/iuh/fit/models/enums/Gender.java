package iuh.fit.models.enums;



import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
    MALE("Nam"), FEMALE("Nữ");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}
