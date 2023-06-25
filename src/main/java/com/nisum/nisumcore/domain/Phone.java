package com.nisum.nisumcore.domain;

import lombok.NonNull;

public record Phone(
    @NonNull String number, @NonNull String cityCode, @NonNull String countryCode) {}
