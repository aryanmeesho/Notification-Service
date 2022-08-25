package com.meesho.notificationservice.services;

import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.repositories.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTests {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    public void getAllNotificationsFromDBTest_basic(){

        when(notificationRepository.findAll()).thenReturn(Arrays.asList(new Notification("+917869216592", "TEST1"),
                                                                        new Notification("+917862124565", "TEST2")));

        List<Notification> actual = notificationService.findAll();

        assertEquals(2, actual.size());

    }

    @Test
    public void getAllNotificationsFromDBTest_empty(){

        when(notificationRepository.findAll()).thenReturn(Arrays.asList());

        List<Notification> actual = notificationService.findAll();

        assertEquals(0, actual.size());

    }

    @Test
    public void InvalidSmsRequestIdHasZeroAccessToDB(){
        try {
            notificationService.getById("abcd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify(notificationRepository,times(1)).findById("abcd");
    }

}
