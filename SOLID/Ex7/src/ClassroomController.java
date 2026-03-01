public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) {
        this.reg = reg;
    }

    public void startClass() {
        PowerDevice projector = reg.getFirst(PowerDevice.class);
        InputConnectable input = reg.getFirst(InputConnectable.class);
        projector.powerOn();
        input.connectInput("HDMI-1");

        BrightnessControl lights = reg.getFirst(BrightnessControl.class);
        lights.setBrightness(60);

        TemperatureControl ac = reg.getFirst(TemperatureControl.class);
        ac.setTemperatureC(24);

        AttendanceScan scan = reg.getFirst(AttendanceScan.class);
        System.out.println("Attendance scanned: present=" + scan.scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        reg.getFirst(Projector.class).powerOff();
        reg.getFirst(LightsPanel.class).powerOff();
        reg.getFirst(AirConditioner.class).powerOff();
    }
}