/**
 * Marabou - Audio Tagger
 * <p>
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * <p>
 * https://github.com/hennr/marabou
 * <p>
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
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
