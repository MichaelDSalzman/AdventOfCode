package aoc2017.day25;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = null;
        String day = null;

        Pattern p = Pattern.compile("aoc(\\d+)\\.day(\\d+).*");
        Matcher m = p.matcher(Main.class.getName());
        if(m.find()) {
            year = m.group(1);
            day = m.group(2);
        }

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        long start = System.currentTimeMillis();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println ("P2 : " + problem.calculateP2(lines));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        public Object calculateP1(List<String> lines) {
            StateMachine machine = StateMachine.valueOf(lines);

            long iterationTimes = Long.parseLong(lines.get(1).split("\\s+")[5]);
            for(long i=0; i< iterationTimes; i++) {
                machine.iterate();
            }
            return machine.getValues().values().stream().filter(v -> v.equals("1")).toList().size();
        }

        public Object calculateP2(List<String> lines) {
            return -1;
        }
    }

    public record StateDecision(String valueToWrite, int cursorDirection, String nextState){}
    public record State(String name, Map<String, StateDecision> stateDecisions) {}

    public static class StateMachine {
        private Map<String, State> states = new HashMap<>();
        private State currentState;
        private int cursorPosition;
        private Map<Integer, String> values = new HashMap<>();

        private StateMachine(Map<String, State> states, String beginningState) {
            this.states = states;
            this.currentState = states.get(beginningState);
            this.cursorPosition = 0;
        }

        public Map<Integer, String> getValues() {
            return new HashMap<>(this.values);
        }

        public static StateMachine valueOf(List<String> lines) {
            String beginningStateName = "A";
            Map<String, State> states = new HashMap<>();

            String stateName = null;
            String decisionValue = null;
            String decisionWriteValue = null;
            int decisionDirection = 0;
            String decisionNextState = null;
            Map<String, StateDecision> decisions = new HashMap<>();

            for(String line : lines) {
                if(line.startsWith("In state")) {
                    if(stateName != null) {
                        StateDecision decision = new StateDecision(decisionWriteValue, decisionDirection, decisionNextState);
                        decisions.put(decisionValue, decision);

                        State state = new State(stateName, decisions);
                        states.put(stateName, state);
                    }
                    decisionValue = null;
                    decisionWriteValue = null;
                    decisionDirection = 0;
                    decisionNextState = null;
                    decisions = new HashMap<>();

                    stateName = line.split("\\s+")[2].substring(0, 1);
                }
                else if(line.contains("If the current value")) {
                    if(decisionValue != null) {
                        StateDecision decision = new StateDecision(decisionWriteValue, decisionDirection, decisionNextState);
                        decisions.put(decisionValue, decision);
                    }
                    decisionValue = line.trim().split("\\s+")[5].substring(0,1);
                } else if(line.contains("Write the value")) {
                    decisionWriteValue = line.trim().split("\\s+")[4].substring(0,1);
                } else if(line.contains("Move one slot")) {
                    if(line.trim().split("\\s+")[6].startsWith("left")) {
                        decisionDirection = -1;
                    } else {
                        decisionDirection = 1;
                    }
                } else if(line.contains("Continue with state")) {
                    decisionNextState = line.trim().split("\\s+")[4].substring(0,1);
                }
            }

            StateDecision decision = new StateDecision(decisionWriteValue, decisionDirection, decisionNextState);
            decisions.put(decisionValue, decision);

            State state = new State(stateName, decisions);
            states.put(stateName, state);

            return new StateMachine(states, beginningStateName);
        }

        public void iterate() {
            String currentValue = this.values.getOrDefault(this.cursorPosition, "0");
            StateDecision stateDecision = this.currentState.stateDecisions.get(currentValue);

            values.put(this.cursorPosition, stateDecision.valueToWrite);
            cursorPosition += stateDecision.cursorDirection;
            this.currentState = states.get(stateDecision.nextState);
        }
    }
}
