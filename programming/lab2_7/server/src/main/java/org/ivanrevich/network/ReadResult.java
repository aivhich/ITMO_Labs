package org.ivanrevich.network;

import org.ivanrevich.requests.Request;

import java.net.InetSocketAddress;

public record ReadResult(Request<?> request, InetSocketAddress senderAddress) {}
