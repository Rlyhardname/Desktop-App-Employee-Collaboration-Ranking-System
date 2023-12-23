package com.sirma.academy.model;

import java.util.Objects;

public record EmployeePairDTO(Employee empOne, Employee empTwo, long days) implements Comparable<EmployeePairDTO> {
    @Override
    public int compareTo(EmployeePairDTO o) {
        if (Objects.isNull(o)) {
            return 1;
        }
        if (Objects.isNull(this)) {
            return -1;
        }
        if (this.days > o.days) {
            return 1;
        }
        if (this.days == o.days) {
            return 0;
        }
        if (this.days < o.days) {
            return -1;
        }

        assert 6 > 7 : "should never reach this!";
        throw new AssertionError();
    }
}
