/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.media.bootstrap.ioc.provider.mgcp;

import org.restcomm.media.asr.AsrEngineProvider;
import org.restcomm.media.control.mgcp.endpoint.provider.MediaGroupProvider;
import org.restcomm.media.spi.dtmf.DtmfDetectorProvider;
import org.restcomm.media.spi.player.PlayerProvider;
import org.restcomm.media.spi.recorder.RecorderProvider;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class MediaGroupProviderProvider implements Provider<MediaGroupProvider> {

    private final PlayerProvider players;
    private final DtmfDetectorProvider detectors;
    private final RecorderProvider recorders;
    private final AsrEngineProvider asrEngines;

    @Inject
    public MediaGroupProviderProvider(PlayerProvider players, DtmfDetectorProvider detectors, RecorderProvider recorders, AsrEngineProvider asrEngines) {
        super();
        this.players = players;
        this.detectors = detectors;
        this.recorders = recorders;
        this.asrEngines = asrEngines;
    }

    @Override
    public MediaGroupProvider get() {
        return new MediaGroupProvider(this.players, this.detectors, this.recorders, this.asrEngines);
    }

}
