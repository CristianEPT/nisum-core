package com.nisum.nisumcore.adapter.in.web.model;

import java.util.List;
import lombok.NonNull;

public record UserRequest(
    @NonNull String name,
    @NonNull String email,
    @NonNull String password,
    List<PhoneRequest> phones) {}
