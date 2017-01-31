package org.emot.libcontrol;


class AnimationFactory {
    static Runnable getRunnable(Emotions which) {
        switch (which) {
            case RESET:
                return new ResetAnimation();
            case ANGRY:
                return new AngryAnimation();
            case HAPPY:
                return new HappyAnimation();
            case SAD:
                return new SadAnimation();
            case FEAR:
                return new FearAnimation();
            case DISGUST:
                return new DisgustAnimation();
            case SURPRISE:
                return new SurpriseAnimation();
            default:
                return null;
        }
    }

    private static class ResetAnimation implements Runnable {

        @Override
        public void run() {
            EmotControl.setLed(Leds.LEFT, LedColors.BLACK);
            EmotControl.setLed(Leds.RIGHT, LedColors.BLACK);
            EmotControl.setArm(Arms.LEFT, ArmActions.STABLE);
            EmotControl.setArm(Arms.RIGHT, ArmActions.STABLE);
        }
    }

    private static class AngryAnimation implements Runnable {

        @Override
        public void run() {
            System.out.println("dajizi");
        }
    }

    private static class HappyAnimation implements Runnable {

        @Override
        public void run() {
            System.out.println("dajizi");
        }
    }

    private static class SadAnimation implements Runnable {

        @Override
        public void run() {
            System.out.println("dajizi");
        }
    }

    private static class FearAnimation implements Runnable {

        @Override
        public void run() {
            System.out.println("dajizi");
        }
    }

    private static class DisgustAnimation implements Runnable {

        @Override
        public void run() {
            System.out.println("dajizi");
        }
    }

    private static class SurpriseAnimation implements Runnable {

        @Override
        public void run() {
            System.out.println("dajizi");
        }
    }
}
