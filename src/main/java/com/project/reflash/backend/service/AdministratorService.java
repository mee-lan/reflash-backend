package com.project.reflash.backend.service;


import com.project.reflash.backend.dto.Administrator;
import com.project.reflash.backend.exception.ExceptionMessage;
import com.project.reflash.backend.exception.UserDoesNotExistException;
import com.project.reflash.backend.repository.AdministratorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    AdministratorService(AdministratorRepository administratorRepository ) {
        this.administratorRepository = administratorRepository;
    }


}
