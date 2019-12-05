package com.motus.workflow.driverprocessing;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Arguments {

    /**
     * Usernames to process
     */
    private List<String> usernames = new ArrayList<>();
}
