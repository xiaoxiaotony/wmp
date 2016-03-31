package com.txy.capture.task;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Packet {

	protected ByteBuffer byteBuf;

	protected static final int MAGIC_HEAD = 0x52434D53;
	protected static final short VER_HEAD = 1;
	public static final int HEAD_LENGTH = 12;

	public Packet() {
	}

	public Packet(short msg_id, String strBody) {
		byte body_gbk[] = null;
		try {
			body_gbk = strBody.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		byte[] head_buf = MakeHead(msg_id, body_gbk.length);
		byteBuf = ByteBuffer.allocate(HEAD_LENGTH + body_gbk.length);
		byteBuf.put(head_buf);
		byteBuf.put(body_gbk);

		byteBuf.flip();
	}

	public ByteBuffer getBuf() {
		return byteBuf;
	}

	public byte[] MakeHead(short msg_id, int len) {
		ByteBuffer buffer = ByteBuffer.allocate(HEAD_LENGTH);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(MAGIC_HEAD);
		buffer.putShort(VER_HEAD);
		buffer.putShort(msg_id);
		buffer.putInt(len);
		return buffer.array();
	}

	public int ParseHead(ByteBuffer head) {
		head.order(ByteOrder.LITTLE_ENDIAN);
		head.flip();
		int magic = head.getInt();
		if (magic != MAGIC_HEAD) {
			return -1;
		}
		short ver = head.getShort();
		if (ver != VER_HEAD) {
			return -1;
		}
		short msg_id = head.getShort();
		System.out.println(msg_id);
		return head.getInt();
	}
}
