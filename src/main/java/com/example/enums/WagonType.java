package com.example.enums;

import java.util.Set;

public enum WagonType {

    TANKER(Set.of(CargoType.LIQUID, CargoType.HAZARDOUS)),
    BOX(Set.of(CargoType.DRY_BULK, CargoType.CONTAINER)),
    FLAT(Set.of(CargoType.CONTAINER, CargoType.HEAVY_MACHINERY, CargoType.STEEL, CargoType.TIMBER));

    private final Set<CargoType> allowedCargoTypes;

    WagonType(Set<CargoType> allowedCargoTypes) {
        this.allowedCargoTypes = allowedCargoTypes;
    }

    public Set<CargoType> getAllowedCargoTypes() {
        return allowedCargoTypes;
    }
}