package com.allthing.hermesbackend.application.domain;

import org.quartz.CronExpression;

import java.util.UUID;

public record Message(UUID publicId, String title, String body, CronExpression scheduledTime, String creatorUsername) {
}
