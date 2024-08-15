package com.allthing.hermesbackend.application.domain;

import org.quartz.CronExpression;

public record Message(String title, String body, CronExpression scheduledTime) {
}
