package it.angelic.soulissclient.net;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import it.angelic.soulissclient.Constants;
import it.angelic.soulissclient.SoulissApp;
import it.angelic.soulissclient.helpers.SoulissPreferenceHelper;

import static it.angelic.soulissclient.Constants.TAG;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Static methods to build requests' frames
 * 
 * 
 * @author shine@angelic.it
 * 
 */
public class UDPHelper {

	/**
	 * Issue a command to Souliss, at specified coordinates
	 * 
	 * @param id
	 * @param slot
	 * @param cmd
	 * @return TODO output string
	 */
	public static String issueSoulissCommand(String id, String slot, SoulissPreferenceHelper prefs,
			String... cmd) {
		InetAddress serverAddr;
		DatagramSocket sender = null;
		DatagramPacket packet;
		try {
			// Log.d(TAG, "Issuing command " + cmd[0]);
			serverAddr = InetAddress.getByName(prefs.getAndSetCachedAddress());

			sender = getSenderSocket(serverAddr);
			ArrayList<Byte> buf;
			if (id.equals(""+it.angelic.soulissclient.Constants.MASSIVE_NODE_ID) ){
				buf = buildVNetFrame(buildMaCaCoMassive(Constants.Net.Souliss_UDP_function_force_massive, slot, cmd),
						prefs.getPrefIPAddress(), prefs.getUserIndex(), prefs.getNodeIndex());
			} else {
				buf = buildVNetFrame(buildMaCaCoForce(Constants.Net.Souliss_UDP_function_force, id, slot, cmd),
						prefs.getPrefIPAddress(), prefs.getUserIndex(), prefs.getNodeIndex());
			}

			byte[] merd = new byte[buf.size()];
			for (int i = 0; i < buf.size(); i++) {
				merd[i] = buf.get(i);
			}
			packet = new DatagramPacket(merd, merd.length, serverAddr, prefs.getUDPPort());

			sender.send(packet);
			Log.i(Constants.Net.TAG, "<-- issueSoulissCommand " + cmd[0] + " sent to: " + serverAddr);

			return "UDP command OK";
		} catch (UnknownHostException ed) {
            Log.e(Constants.Net.TAG, "***UnknownHostException: " + ed.getMessage());
            return ed.getLocalizedMessage();
		} catch (SocketException et) {
            Log.e(Constants.Net.TAG, "***SocketException: " + et.getMessage());
            return et.getLocalizedMessage();
		} catch (Exception e) {
            Log.e(Constants.Net.TAG, "***issueSoulissCommand Failure:", e);
            return e.getLocalizedMessage();
		} finally {
			if (sender != null && !sender.isClosed())
				sender.close();
		}
	}


	public static void issueBroadcastConfigure(SoulissPreferenceHelper prefs, int functional, List<Byte> bcastPayload,@Nullable Boolean isGw, Boolean useDhcp) {
		InetAddress serverAddr;
		DatagramSocket sender = null;
		DatagramPacket packet;
		try {

			String ip = Constants.Net.BROADCASTADDR;
			serverAddr = InetAddress.getByName(ip);
			sender = getSenderSocket(serverAddr);
			
			//bcastPayload = Arrays.asList(Constants.PING_BCAST_PAYLOAD);
			ArrayList<Byte> macacoPay = new ArrayList<>();
			sender.setBroadcast(true);

			switch (functional){
				case Constants.Net.Souliss_UDP_function_broadcast_configure:
					macacoPay = UDPHelper.buildMaCaCoBroadCastConfigure(isGw,useDhcp, bcastPayload);
					break;
				case Constants.Net.Souliss_UDP_function_broadcast_configure_wifissid:
					macacoPay = UDPHelper.buildMaCaCoBroadCastConfigureWifiSsid(bcastPayload);
					break;
				case Constants.Net.Souliss_UDP_function_broadcast_configure_wifipass:
					macacoPay = UDPHelper.buildMaCaCoBroadCastConfigureWifiPass(bcastPayload);
					break;
			}
			//bcastPayload.set(1, whoami);

			ArrayList<Byte> buf = UDPHelper.buildVNetFrame(macacoPay, ip,prefs.getUserIndex(),
					prefs.getNodeIndex());
			/* indirizzo ip
			  (4 byte) | subnetmask (4 byte) | gateway ip (4 byte) | lunghezza SSID (1
					 * byte) | lunghezza password (1 byte) | SSID (lunghezza SSID byte) | password
					 * (lunghezza password byte)*/
			byte[] merd = new byte[buf.size()];
			for (int i = 0; i < buf.size(); i++) {
				merd[i] = buf.get(i);
			}
			packet = new DatagramPacket(merd, merd.length, serverAddr,  prefs.getUDPPort());

			sender.send(packet);
			Log.w(Constants.Net.TAG, "***BROADCAST sent to: " + serverAddr);
			//	Log.d(Constants.TAG, "***BYTES: " + buf.toString());
			
		} catch (UnknownHostException | SocketException ed) {
			ed.printStackTrace();
		} catch (Exception e) {
			Log.e(Constants.Net.TAG, "***Fail", e);
		} finally {
			if (sender != null && !sender.isClosed())
				sender.close();
		}
	}

	/**
	 * Builds a broadcast command to all typicals of the same type
	 * 
	 * @param typ
	 * @param prefs
	 * @param cmd
	 *            varargs of commands
	 * @return
	 */
	public static String issueMassiveCommand(String typ, SoulissPreferenceHelper prefs, String... cmd) {
		InetAddress serverAddr;
		DatagramSocket sender = null;
		DatagramPacket packet;
		try {
			// Log.d(TAG, "Issuing command " + cmd[0]);
			serverAddr = InetAddress.getByName(prefs.getAndSetCachedAddress());

			sender = getSenderSocket(serverAddr);
			ArrayList<Byte> buf;
			buf = buildVNetFrame(buildMaCaCoMassive(Constants.Net.Souliss_UDP_function_force_massive, typ, cmd),
					prefs.getPrefIPAddress(), prefs.getUserIndex(), prefs.getNodeIndex());

			byte[] merd = new byte[buf.size()];
			for (int i = 0; i < buf.size(); i++) {
				merd[i] = buf.get(i);
			}
			packet = new DatagramPacket(merd, merd.length, serverAddr,  prefs.getUDPPort());

			sender.send(packet);
			Log.i(Constants.Net.TAG, "<-- Massive Command sent to: " + serverAddr);

			return "UDP massive command OK";
		} catch (UnknownHostException ed) {
			ed.printStackTrace();
			return ed.getLocalizedMessage();
		} catch (SocketException et) {
			et.printStackTrace();
			return et.getLocalizedMessage();
		} catch (Exception e) {
			Log.d(Constants.Net.TAG, "***Fail", e);
			return e.getLocalizedMessage();
		} finally {
			if (sender != null && !sender.isClosed())
				sender.close();
		}
	}

	/**
	 * * N+1 N 17 B1 00 64 01 08 00 00 00 00 ipubbl can be = to ip, if local
	 * area LAN is used
	 * 
	 * @return contacted address
	 * 
	 * @param ip
	 *            private LAN IP address, mandatory
	 * @param ipubbl
	 *            public IP, if any
	 * @param userix
	 *            Souliss User index
	 * @param nodeix
	 *            Souliss Node index
	 * 
	 * @throws Exception
	 *             catch not implemented by design
	 */
	public static InetAddress ping(String ip, String ipubbl, int userix, int nodeix, @Nullable SoulissPreferenceHelper pref) throws Exception {

		InetAddress serverAddr;
		DatagramSocket sender = null;
		DatagramPacket packet;

		try {

			serverAddr = InetAddress.getByName(ipubbl);

			DatagramChannel channel = DatagramChannel.open();
			sender = channel.socket();
			sender.setReuseAddress(true);

			// hole punch
			InetSocketAddress sa = new InetSocketAddress(Constants.Net.SERVERPORT);

			List<Byte> macaco = new ArrayList<>();
			macaco = Arrays.asList(Constants.Net.PING_PAYLOAD);
			// qui inserisco broadcast
			byte whoami = 0xB;// PRIVATE by default
			if (ipubbl.compareTo(ip) == 0)
				whoami = 0xF;
			else if (ipubbl.compareTo(Constants.Net.BROADCASTADDR) == 0) {
				whoami = 0x5;
				macaco = Arrays.asList(Constants.Net.PING_BCAST_PAYLOAD);
				ip = Constants.Net.BROADCASTADDR;
				sender.setBroadcast(true);
			}
			macaco.set(1, whoami);
			ArrayList<Byte> buf = UDPHelper.buildVNetFrame(macaco, ip, userix, nodeix);

			byte[] merd = new byte[buf.size()];
			for (int i = 0; i < buf.size(); i++) {
				merd[i] = buf.get(i);
			}
			sender.bind(sa);
			packet = new DatagramPacket(merd, merd.length, serverAddr,  pref.getUDPPort());
			sender.send(packet);
			Log.i(Constants.Net.TAG, "<-- Ping sent to: " + serverAddr);
			debugByteArray(buf);
			return serverAddr;
		} finally {
			if (sender != null && !sender.isClosed())
				sender.close();
		}

	}

	/**
	 * N+1 N 17 B1 00 64 01 08 00 00 00 00 used to recreate DB
	 * 
	 */
	public static void dbStructRequest(SoulissPreferenceHelper prefs) {

		InetAddress serverAddr;
		DatagramSocket sender = null;
		DatagramPacket packet;

		try {
			serverAddr = InetAddress.getByName(prefs.getAndSetCachedAddress());
			sender = getSenderSocket(serverAddr);

			List<Byte> macaco = new ArrayList<>();
			macaco = Arrays.asList(Constants.Net.DBSTRUCT_PAYLOAD);
			ArrayList<Byte> buf = UDPHelper.buildVNetFrame(macaco, prefs.getPrefIPAddress(), prefs.getUserIndex(),
					prefs.getNodeIndex());

			byte[] merd = new byte[buf.size()];
			for (int i = 0; i < buf.size(); i++) {
				merd[i] = buf.get(i);
			}
			packet = new DatagramPacket(merd, merd.length, serverAddr,  prefs.getUDPPort());
			sender.send(packet);
			Log.w(Constants.Net.TAG, "<-- dbStructRequest bytes:" + packet.getLength());
			return;
		} catch (UnknownHostException | SocketException ed) {
			Log.d(Constants.Net.TAG, "***requestDBStruct Fail", ed);
			return;
		} catch (Exception e) {
			Log.d(Constants.Net.TAG, "***requestDBStruct Fail", e);
			return;
		} finally {
			if (sender != null && !sender.isClosed())
				sender.close();
		}

	}

	/**
	 * Subscribe request.
	 * 
	 * @param prefs
	 *            App preferences
	 * @param numberOf
	 *            number of nodes to request
	 * @param startOffset
	 */
	public static void stateRequest(SoulissPreferenceHelper prefs, int numberOf, int startOffset) {

		InetAddress serverAddr;
		DatagramSocket sender = null;
		DatagramPacket packet;

		try {
			serverAddr = InetAddress.getByName(prefs.getAndSetCachedAddress());
			sender = getSenderSocket(serverAddr);

			List<Byte> macaco = new ArrayList<>();
			macaco.add(Constants.Net.Souliss_UDP_function_subscribe);
			// PUTIN, STARTOFFEST, NUMBEROF
			macaco.add((byte) 0x0);// PUTIN
			macaco.add((byte) 0x0);// PUTIN

			macaco.add((byte) startOffset);// startnode
			macaco.add((byte) numberOf);// numberof

			ArrayList<Byte> buf = UDPHelper.buildVNetFrame(macaco, prefs.getPrefIPAddress(), prefs.getUserIndex(),
					prefs.getNodeIndex());

			byte[] merd = toByteArray(buf);
			packet = new DatagramPacket(merd, merd.length, serverAddr,  prefs.getUDPPort());
			sender.send(packet);
			Log.i(Constants.Net.TAG, "<-- stateRequest sent. bytes:" + packet.getLength() + ", numberof=" + numberOf);
		} catch (UnknownHostException ed) {
			Log.e(Constants.Net.TAG, "***stateRequest Fail", ed);
			return;
		} catch (SocketException et) {
			Log.e(Constants.Net.TAG, "***stateRequest Fail", et);
			return;
		} catch (Exception e) {
			Log.e(Constants.Net.TAG, "***stateRequest Fail", e);
			return;
		} finally {
			if (sender != null && !sender.isClosed())
				sender.close();
		}

	}

	/**
	 * Poll data request, without data subscription (one-shot)
	 * 
	 * @param prefs
	 */
	public static void pollRequest(SoulissPreferenceHelper prefs, int numberOf, int startOffset) {

		InetAddress serverAddr;
		DatagramSocket sender = null;
		DatagramPacket packet;

		try {
			serverAddr = InetAddress.getByName(prefs.getAndSetCachedAddress());
			Log.i(Constants.Net.TAG, "<-- Poll request, numberof=" + numberOf + " offset=" + startOffset);
			sender = getSenderSocket(serverAddr);

			List<Byte> macaco = new ArrayList<>();
			macaco.add(Constants.Net.Souliss_UDP_function_poll);
			// PUTIN, STARTOFFEST, NUMBEROF
			macaco.add((byte) 0x0);// PUTIN
			macaco.add((byte) 0x0);// PUTIN

			macaco.add((byte) startOffset);// startnode
			macaco.add((byte) numberOf);// numberof

			ArrayList<Byte> buf = UDPHelper.buildVNetFrame(macaco, prefs.getPrefIPAddress(), prefs.getUserIndex(),
					prefs.getNodeIndex());

			// pessimo
			// http://stackoverflow.com/questions/6860055/convert-arraylistbyte-into-a-byte
			byte[] merd = toByteArray(buf);
			packet = new DatagramPacket(merd, merd.length, serverAddr, prefs.getUDPPort());
			sender.send(packet);
			Log.i(Constants.Net.TAG, "<-- poll Request sent. bytes:" + packet.getLength());
		} catch (UnknownHostException ed) {
			Log.e(Constants.Net.TAG, "***stateRequest Fail", ed);
			return;
		} catch (SocketException et) {
			Log.e(Constants.Net.TAG, "***stateRequest Fail", et);
			return;
		} catch (Exception e) {
			Log.e(Constants.Net.TAG, "***stateRequest Fail", e);
			return;
		} finally {
			if (sender != null && !sender.isClosed())
				sender.close();
		}

	}

	/**
	 * Trigger a structural request for given device
	 * @param prefs
	 * @param numberOf number of desired nodes' devices
	 * @param startOffset node offset
	 */
	public static void typicalRequest(SoulissPreferenceHelper prefs, int numberOf, int startOffset) {

		assertEquals(true, numberOf < 128);
		InetAddress serverAddr;
		DatagramSocket sender = null;
		DatagramPacket packet;

		try {
			serverAddr = InetAddress.getByName(prefs.getAndSetCachedAddress());
			sender = getSenderSocket(serverAddr);
			Log.i(Constants.Net.TAG, "<-- typicalRequest, numberof=" + numberOf);
			List<Byte> macaco = new ArrayList<>();
			// PUTIN, STARTOFFEST, NUMBEROF
			macaco.add(Constants.Net.Souliss_UDP_function_typreq);
			macaco.add((byte) 0x0);// PUTIN
			macaco.add((byte) 0x0);// PUTIN
			macaco.add((byte) startOffset);// startnode
			macaco.add((byte) numberOf);// numberof

			ArrayList<Byte> buf = UDPHelper.buildVNetFrame(macaco, prefs.getPrefIPAddress(), prefs.getUserIndex(),
					prefs.getNodeIndex());

			byte[] merd = toByteArray(buf);
			packet = new DatagramPacket(merd, merd.length, serverAddr, prefs.getUDPPort());
			sender.send(packet);
			Log.i(Constants.Net.TAG, "<-- typRequest sent to " + serverAddr.getHostAddress());
		} catch (UnknownHostException ed) {
			ed.printStackTrace();
			return;
		} catch (SocketException et) {
			et.printStackTrace();
			return;
		} catch (Exception e) {
			Log.e(Constants.Net.TAG, "typRequest Fail", e);
			return;
		} finally {
			if (sender != null && !sender.isClosed())
				sender.close();
		}
	}

	public static void healthRequest(SoulissPreferenceHelper prefs, int numberOf, int startOffset) {

		assertEquals(true, numberOf < 128);
		assertEquals(true, prefs.getPrefIPAddress() != null);
		InetAddress serverAddr;
		DatagramSocket sender = null;
		DatagramPacket packet;

		try {
			Log.d(Constants.Net.TAG, "<-- HealthRequest, numberof=" + numberOf);
			serverAddr = InetAddress.getByName(prefs.getAndSetCachedAddress());
			sender = getSenderSocket(serverAddr);

			List<Byte> macaco = new ArrayList<>();
			// PUTIN, STARTOFFEST, NUMBEROF
			macaco.add(Constants.Net.Souliss_UDP_function_healthReq);
			macaco.add((byte) 0x0);// PUTIN
			macaco.add((byte) 0x0);// PUTIN
			macaco.add((byte) startOffset);// startnode
			macaco.add((byte) numberOf);// numberof

			ArrayList<Byte> buf = UDPHelper.buildVNetFrame(macaco, prefs.getPrefIPAddress(), prefs.getUserIndex(),
					prefs.getNodeIndex());

			byte[] merd = toByteArray(buf);
			packet = new DatagramPacket(merd, merd.length, serverAddr,  prefs.getUDPPort());
			sender.send(packet);
			Log.w(Constants.Net.TAG, "<-- healthRequest sent to " + serverAddr.getHostAddress());
		} catch (UnknownHostException ed) {
			Log.e(Constants.Net.TAG, "Souliss unavailable " + ed.getMessage());
			return;
		} catch (SocketException et) {
			Log.e(Constants.Net.TAG, "typRequest Fail", et);
			return;
		} catch (Exception e) {
			Log.e(Constants.Net.TAG, "typRequest Fail", e);
			return;
		} finally {
			if (sender != null && !sender.isClosed())
				sender.close();
		}

	}

	/**
	 * Wrappa una ping, per causare una risposta da souliss ip puo essere quello
	 * pubblico, privato o broadcast
	 * 
	 * local address is always necessary
	 * 
	 * @param timeoutMsec
	 * @param prefs
	 *            Souliss preferences
	 * @param ip
	 */
	public static String checkSoulissUdp(int timeoutMsec, SoulissPreferenceHelper prefs, String ip) {


		// assertEquals(true, ip.equals(prefs.getIPPreferencePublic()) ||
		// ip.equals(prefs.getPrefIPAddress()));

		try {
			return ping(prefs.getPrefIPAddress(), ip, prefs.getUserIndex(), prefs.getNodeIndex(),prefs).getHostAddress();
		} catch (UnknownHostException ed) {
			Log.e(Constants.Net.TAG, "***UnknownHostFail", ed);
			return ed.getMessage();
		} catch (SocketException et) {
			Log.e(Constants.Net.TAG, "***SocketFail", et);
			return et.getMessage();
		} catch (Exception e) {
			Log.e(Constants.Net.TAG, "***Fail", e);
			return e.getMessage();
		}

	}

	/**
	 * Costruzione frame vNet: 0D 0C 17 11 00 64 01 XX 00 00 00 01 01 0D è la
	 * lunghezza complessiva del driver vNet 0C è la lunghezza complessiva vNet
	 * 17 è la porta MaCaco su vNet (fissa)
	 * 
	 * 11 00 è l'indirizzo della scheda quindi un indirizzo IP con ultimo byte
	 * 100 va SEMPRE passato l'indirizzo privato della scheda
	 * 
	 * 64 01 è l'indirizzo dell'interfaccia utente, 01 è l'User Mode Index
	 * 
	 * @param macaco
	 *            frame input
	 * @return
	 */
	private static ArrayList<Byte> buildVNetFrame(List<Byte> macaco, String ipd, int useridx, int nodeidx) {

		assertEquals(true, useridx < it.angelic.soulissclient.Constants.MAX_USER_IDX);
		assertEquals(true, nodeidx < it.angelic.soulissclient.Constants.MAX_NODE_IDX);

		ArrayList<Byte> frame = new ArrayList<>();
		InetAddress ip;
		try {
			ip = InetAddress.getByName(ipd);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return frame;
		}
		byte[] dude = ip.getAddress();
		frame.add((byte) 23);// PUTIN

		frame.add(dude[3]);// es 192.168.1.XX BOARD
		// n broadcast : La comunicazione avviene utilizzando l'indirizzo IP
		// 255.255.255.255 a cui associare l'indirizzo vNet 0xFFFF.
		frame.add((byte) ipd.compareTo(Constants.Net.BROADCASTADDR) == 0 ? dude[2] : 0);
		// 192.168.XX.0
		frame.add((byte) nodeidx); // NODE INDEX
		frame.add((byte) useridx);// USER IDX

		// aggiunge in testa il calcolo
		frame.add(0, (byte) (frame.size() + macaco.size() + 1));
		frame.add(0, (byte) (frame.size() + macaco.size() + 1));// Check 2

		frame.addAll(macaco);

		//Byte[] ret = new Byte[frame.size()];

		debugByteArray(frame);

		// Send broadcast timeout
		Intent i = new Intent();
		int it = SoulissApp.getOpzioni().getRemoteTimeoutPref() + SoulissApp.getOpzioni().getBackoff() * 1000;
		Log.d(TAG, "buildVNetFrame Posting timeout msec. " + it);
		i.putExtra("REQUEST_TIMEOUT_MSEC", it);
		i.setAction(Constants.CUSTOM_INTENT_SOULISS_TIMEOUT);
		SoulissApp.getOpzioni().getContx().sendBroadcast(i);

		return frame;
	}

	private static void debugByteArray(ArrayList<Byte> frame) {
		StringBuilder deb = new StringBuilder();
		for (int i = 0; i < frame.size(); i++) {
			deb.append("0x").append(Long.toHexString((long) frame.get(i) & 0xff)).append(" ");
		}
		Log.d(Constants.Net.TAG, "frame debug: " + deb.toString());
	}

	/**
	 * Builds a Macaco frame to issue a standard command
	 * 
	 * @param functional
	 * @param nodeId
	 *            Node's id
	 * @param slot
	 * @param cmd
	 * @return
	 */
	private static ArrayList<Byte> buildMaCaCoForce(byte functional, String nodeId, String slot, String... cmd) {
		assertEquals(true, functional < Byte.MAX_VALUE);
		ArrayList<Byte> frame = new ArrayList<>();

		frame.add(functional);// functional code

		frame.add(Byte.valueOf("0"));// PUTIN
		frame.add(Byte.valueOf("0"));

		frame.add(Byte.valueOf(nodeId)); // STARTOFFSET
		frame.add(((byte) (Byte.valueOf(slot) + cmd.length))); // NUMBEROF

		for (int i = 0; i <= Byte.valueOf(slot); i++) {
			if (i == Byte.valueOf(slot)) {

				for (String number : cmd) {
					// che schifo
					int merdata = Integer.decode(number);
					if (merdata > 255) {
						// TODO chiedere a Dario
						Log.w(Constants.Net.TAG, "Overflow with command " + number);
					}
					frame.add((byte) merdata);
				}

				break;// ho finito un comando su piu bytes
			} else
				frame.add(Byte.valueOf("0"));
		}

		Log.v(Constants.Net.TAG, "MaCaCo frame built size:" + frame.size());
		return frame;

	}

	private static ArrayList<Byte> buildMaCaCoMassive(byte functional, String typical, String... cmd) {
		assertEquals(true, functional < Byte.MAX_VALUE);
		ArrayList<Byte> frame = new ArrayList<>();

		frame.add(functional);// functional code

		frame.add(Byte.valueOf("0"));// PUTIN
		frame.add(Byte.valueOf("0"));

		frame.add(Byte.valueOf(typical)); // STARTOFFSET
		frame.add((byte) cmd.length); // NUMBEROF

		for (String number : cmd) {
			// che schifo
			int merdata = Integer.decode(number);
			if (merdata > 255)
				Log.w(Constants.Net.TAG, "Overflow with command string: " + number);
			frame.add((byte) merdata);
		}

		Log.d(Constants.Net.TAG, "MaCaCo MASSIVE frame built size:" + frame.size());
		return frame;

	}

	private static ArrayList<Byte> buildMaCaCoBroadCastConfigure(boolean isGateway,boolean useDhcp, List<Byte> payLoad) {
		ArrayList<Byte> frame = new ArrayList<>();

		frame.add(Constants.Net.Souliss_UDP_function_broadcast_configure);// functional code
		Log.d(Constants.Net.TAG, "MaCaCo  BroadCastConfigure isGateway:" + isGateway + " useDhcp:" + useDhcp);
		frame.add(Byte.valueOf("0"));// PUTIN
		frame.add(Byte.valueOf("0"));
		assertTrue(payLoad.size() == 0XC);
		byte startOffset;
		if (useDhcp){
			if (isGateway)
				startOffset=0x2;
			else
				startOffset=0x4;
		}else{
			if (isGateway)
				startOffset=0x1;
			else
				startOffset=0x3;
		}


		frame.add(startOffset); // STARTOFFSET
		frame.add((byte) payLoad.size()); // NUMBEROF

		for (Byte number : payLoad) {
			// che schifo
			frame.add(number);
		}
		Log.d(Constants.Net.TAG, "MaCaCo  BroadCastConfigure frame built size:" + frame.size());

		return frame;

	}

	private static ArrayList<Byte> buildMaCaCoBroadCastConfigureWifiSsid(List<Byte> payLoad) {
		ArrayList<Byte> frame = new ArrayList<>();

		frame.add(Constants.Net.Souliss_UDP_function_broadcast_configure_wifissid);// functional code

		frame.add(Byte.valueOf("0"));// PUTIN
		frame.add(Byte.valueOf("0"));

		frame.add((byte) 0x0); // STARTOFFSET
		frame.add((byte) payLoad.size()); // NUMBEROF

		for (Byte number : payLoad) {
			// che schifo
			frame.add(number);
		}

		Log.d(Constants.Net.TAG, "MaCaCo MASSIVE frame built size:" + frame.size());
		return frame;

	}

	private static ArrayList<Byte> buildMaCaCoBroadCastConfigureWifiPass(List<Byte> payLoad) {
		ArrayList<Byte> frame = new ArrayList<>();

		frame.add(Constants.Net.Souliss_UDP_function_broadcast_configure_wifipass);// functional code

		frame.add(Byte.valueOf("0"));// PUTIN
		frame.add(Byte.valueOf("0"));

		frame.add((byte) 0x0); // STARTOFFSET
		frame.add((byte) payLoad.size()); // NUMBEROF

		for (Byte number : payLoad) {
			// che schifo
			frame.add(number);
		}

		Log.d(Constants.Net.TAG, "MaCaCo MASSIVE frame built size:" + frame.size());
		return frame;

	}

	/**
	 * Builds old-school byte array
	 * 
	 * @param buf
	 * @return
	 */
	private static byte[] toByteArray(ArrayList<Byte> buf) {
		byte[] merd = new byte[buf.size()];
		for (int i = 0; i < buf.size(); i++) {
			merd[i] = buf.get(i);
		}
		return merd;
	}

	/**
	 * Questo e` un metodo fico
	 * 
	 * @param serverAddr
	 * @return
	 */
	private static DatagramSocket getSenderSocket(InetAddress serverAddr) {
		DatagramSocket sender = null;
		try {
			DatagramChannel channel = DatagramChannel.open();
			sender = channel.socket();
			sender.setReuseAddress(true);
			// hole punch
			InetSocketAddress sa = new InetSocketAddress(Constants.Net.SERVERPORT);
			sender.bind(sa);
		} catch (SocketException e) {
			Log.e(Constants.Net.TAG, "SOCKETERR: " + e.getMessage());
		} catch (IOException e) {
			Log.e(Constants.Net.TAG, "IOERR: " + e.getMessage());
		}
		return sender;
	}

}
