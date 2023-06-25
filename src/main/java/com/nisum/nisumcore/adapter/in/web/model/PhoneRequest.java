package com.nisum.nisumcore.adapter.in.web.model;

import lombok.NonNull;

public record PhoneRequest(
    @NonNull String number, @NonNull String cityCode, @NonNull String countryCode) {}
