package com.motus.workflow.driverprocessing;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Data
public class Results {

    Map<String, String> results = new HashMap<>();

}
