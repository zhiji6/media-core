/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
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

package org.restcomm.media.rtp.session;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.junit.After;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restcomm.media.rtp.MediaType;
import org.restcomm.media.rtp.RtpChannel;
import org.restcomm.media.rtp.RtpSessionContext;
import org.restcomm.media.rtp.RtpStatistics;
import org.restcomm.media.scheduler.WallClock;
import org.restcomm.media.sdp.format.AVProfile;
import org.restcomm.media.sdp.format.RTPFormats;

import com.google.common.util.concurrent.FutureCallback;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class RtpSessionFsmImplTest {

    private RtpSessionFsm fsm;

    @After
    public void after() {
        if (fsm != null) {
            if (fsm.isStarted()) {
                fsm.terminate();
            }
            fsm = null;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEnterBindingState() {
        // given
        final long ssrc = 12345L;
        final MediaType mediaType = MediaType.AUDIO;
        final WallClock clock = new WallClock();
        final RtpChannel channel = mock(RtpChannel.class);
        final RtpStatistics statistics = new RtpStatistics(clock, ssrc);
        final RTPFormats formats = AVProfile.audio;
        final RtpSessionContext context = new RtpSessionContext(ssrc, mediaType, statistics, formats);
        final RtpSessionFsmImpl fsm = new RtpSessionFsmImpl(context);

        // when
        SocketAddress address = new InetSocketAddress("127.0.0.1", 6000);
        RtpSessionOpenContext bindContext = new RtpSessionOpenContext(channel, address);
        fsm.enterBinding(RtpSessionState.OPENING, RtpSessionState.BINDING, RtpSessionEvent.OPEN, bindContext);

        // then
        verify(channel).bind(eq(address), any(FutureCallback.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testOpenSession() {
        // given
        final long ssrc = 12345L;
        final MediaType mediaType = MediaType.AUDIO;
        final WallClock clock = new WallClock();
        final RtpChannel channel = mock(RtpChannel.class);
        final RtpStatistics statistics = new RtpStatistics(clock, ssrc);
        final RTPFormats formats = AVProfile.audio;
        final RtpSessionContext context = new RtpSessionContext(ssrc, mediaType, statistics, formats);
        this.fsm = RtpSessionFsmBuilder.INSTANCE.build(context);
        final SocketAddress bindAddress = new InetSocketAddress("127.0.0.1", 6000);
        
        
        doAnswer(new Answer<Void>() {
            
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                FutureCallback<Void> callback = invocation.getArgumentAt(0, FutureCallback.class);
                callback.onSuccess(null);
                return null;
            }
            
        }).when(channel).open(any(FutureCallback.class));

        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                FutureCallback<Void> callback = invocation.getArgumentAt(1, FutureCallback.class);
                callback.onSuccess(null);
                return null;
            }

        }).when(channel).bind(eq(bindAddress), any(FutureCallback.class));

        // when
        fsm.start();
        RtpSessionOpenContext openContext = new RtpSessionOpenContext(channel, bindAddress);
        fsm.fire(RtpSessionEvent.OPEN, openContext);

        // then
        verify(channel).open(any(FutureCallback.class));
        verify(channel).bind(eq(bindAddress), any(FutureCallback.class));
        assertEquals(bindAddress, context.getLocalAddress());
        assertEquals(RtpSessionState.OPEN, fsm.getCurrentState());
    }

}
