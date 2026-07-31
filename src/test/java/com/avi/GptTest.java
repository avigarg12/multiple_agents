package com.avi;

import agents.Gpt;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GptTest {
    @Test
    void gptShouldReturnEmptyString() throws InterruptedException {
        Gpt gpt = new Gpt();
        String response = gpt.process("hello");
        assertEquals("",response);
    }
}