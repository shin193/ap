package Exercise.Ex4.E3_15;

public class Letter {
    private String from;
    private String to;
    private String body;

    public Letter(String from, String to) {
        this.from = from;
        this.to = to;
        body = "";
    }

    public void addLine(String line) {
        body = body.concat(line).concat("\n");
    }

    public String getText() {
        String text = "Dear " + to + ":\n\n";
        text = text.concat(body).concat("\n");
        text = text.concat("Sincerely,\n\n");
        text = text.concat(from);
        return text;
    }
}
