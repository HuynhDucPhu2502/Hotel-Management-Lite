package iuh.fit.models.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShiftDaysSchedule {
    MON_WED_FRI("2-4-6"),
    TUE_THU_SAT("3-5-7"),
    SUNDAY("CN");

    private final String shiftDaysSchedule;

    @Override
    public String toString() {
        return shiftDaysSchedule;
    }
}
