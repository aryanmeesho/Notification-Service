package com.meesho.notificationservice.services;

import com.meesho.notificationservice.repositories.BlacklistRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BlacklistServiceTests {


  @InjectMocks
  NotificationServiceImpl notificationService;

  @Mock
  BlacklistRepository blacklistRepository;

  @Mock
  RedisTemplate redisTemplate;



}
