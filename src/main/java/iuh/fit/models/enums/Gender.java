package iuh.fit.models.enums;



import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public enum Gender {
    MALE("Nam"), FEMALE("Nữ");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}
