package com.telegrambot.realdebrid.services.repositories;

import com.telegrambot.realdebrid.services.dtos.UserDTO;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDTO, Integer> {

    public UserDTO getUserDTOByTelegramId(String telegramId);
}
