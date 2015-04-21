/**
 * Marabou - Audio Tagger
 *
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * https://github.com/hennr/marabou
 *
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.audio.store;

import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.AudioFileProperty;

import java.util.Set;

public class AudioFilePropertyChangeEvent {

  private final Set<AudioFile> affectedFiles;
  private final AudioFileProperty audioFileProperty;
  private final String newValue;

  public AudioFilePropertyChangeEvent(Set<AudioFile> affectedFiles, AudioFileProperty audioFileProperty, String newValue) {
    this.affectedFiles = affectedFiles;
    this.audioFileProperty = audioFileProperty;
    this.newValue = newValue;
  }

  public Set<AudioFile> getAffectedFiles() {
    return affectedFiles;
  }

  public AudioFileProperty getAudioFileProperty() {
    return audioFileProperty;
  }

  public String getNewValue() {
    return newValue;
  }
}
