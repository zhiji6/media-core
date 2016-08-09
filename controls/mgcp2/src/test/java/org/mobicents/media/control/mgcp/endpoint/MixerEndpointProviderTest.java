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

package org.mobicents.media.control.mgcp.endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mobicents.media.control.mgcp.connection.MgcpConnectionProvider;
import org.mobicents.media.control.mgcp.endpoint.provider.MediaGroupProvider;
import org.mobicents.media.control.mgcp.endpoint.provider.MgcpMixerEndpointProvider;
import org.mobicents.media.server.component.audio.AudioComponent;
import org.mobicents.media.server.scheduler.PriorityQueueScheduler;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class MixerEndpointProviderTest {

    @Test
    public void testProvide() {
        // given
        final String namespace = "ms/mock/";
        final PriorityQueueScheduler mediaScheduler = mock(PriorityQueueScheduler.class);
        final MgcpConnectionProvider connections = mock(MgcpConnectionProvider.class);
        final MediaGroupProvider mediaGroupProvider = mock(MediaGroupProvider.class);
        final MgcpMixerEndpointProvider provider = new MgcpMixerEndpointProvider(namespace, mediaScheduler, connections, mediaGroupProvider);
        final MediaGroupImpl mediaGroup = mock(MediaGroupImpl.class);
        final AudioComponent audioComponent = mock(AudioComponent.class);

        // when
        when(mediaGroup.getAudioComponent()).thenReturn(audioComponent);
        when(mediaGroupProvider.provide()).thenReturn(mediaGroup);

        MgcpMixerEndpoint endpoint1 = provider.provide();
        MgcpMixerEndpoint endpoint2 = provider.provide();
        MgcpMixerEndpoint endpoint3 = provider.provide();

        // then
        assertEquals(namespace + 1, endpoint1.getEndpointId());
        assertFalse(endpoint1.isActive());
        assertEquals(namespace + 2, endpoint2.getEndpointId());
        assertFalse(endpoint2.isActive());
        assertEquals(namespace + 3, endpoint3.getEndpointId());
        assertFalse(endpoint3.isActive());
    }

}
