package com.github.birulazena.hotel_app_test_task.histogram.repository;

import java.util.Map;

public interface HistogramRepository {
    Map<String, Long> getHistogram(String param);
}
