package testdata.builder;

import com.github.marabou.audio.AudioFile;

public class TestAudioFileBuilder {


    public static AudioFile aValidCompleteAudioFile() {
        return new AudioFile("filePath")
                    .withArtist("Slayer")
                    .withTitle("Angel of death")
                    .withAlbum("Reign in Blood")
                    .withTrack("1")
                    .withYear("1986")
                    .withGenre("Thrash Metal")
                    .withComment("Slayer rules")
                    .withDiscNumber("1")
                    .withComposer("Jeff Hanneman");
    }

    public static AudioFile anotherValidCompleteAudioFile() {
        return new AudioFile("filePath")
                .withArtist("Britney Spears")
                .withTitle("Oops! … I Did It Again")
                .withAlbum("Oops! … I Did It Again")
                .withTrack("1")
                .withYear("2000")
                .withGenre("Pop")
                .withComment("Oh man")
                .withDiscNumber("1")
                .withComposer("Who cares?");
    }

    public static AudioFile anEmptyValidAudioFile() {
        return new AudioFile("filePath")
                .withArtist("")
                .withTitle("")
                .withAlbum("")
                .withTrack("")
                .withYear("")
                .withGenre("")
                .withComment("")
                .withDiscNumber("")
                .withComposer("");
    }
}
