package it.angelic.soulissclient.preferences;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import it.angelic.soulissclient.Constants;
import it.angelic.soulissclient.R;
import it.angelic.soulissclient.SoulissApp;
import it.angelic.soulissclient.helpers.SoulissPreferenceHelper;
import it.angelic.soulissclient.net.NetUtils;
import it.angelic.soulissclient.net.UDPHelper;

/**
 * Per funzionare dovresti aggiungere in SoulissApp un menù di configurazione
 * dove poter inserire i seguenti parametri: 1) Indirizzo IP da assegnare al
 * Gateway 2) Subnetmask 3) Gateway 4) SSID Rete Wireless a cui il nodo deve
 * collegarsi 5) Password rete wireless
 * <p/>
 * Il frame va inviato su vNet in broadcast (come quello per la ricerca del
 * gateway in automatico) con il payload formattato in questo modo: indirizzo ip
 * (4 byte) | subnetmask (4 byte) | gateway ip (4 byte) | lunghezza SSID (1
 * byte) | lunghezza password (1 byte) | SSID (lunghezza SSID byte) | password
 * (lunghezza password byte).
 * <p/>
 * Il functional code associato è SETIP 0x3B.
 * <p/>
 * Questo frame va inviato solo su richiesta dell'utente e non ad ogni avvio e
 * deve esser possibile lasciare qualunque dei parametri non compilato, in quel
 * caso puoi utilizzare tutti zero.
 * <p/>
 * Ad esempio, se dovessero esserci solo le informazioni IP e non quelle per il
 * wifi il frame sarebbe:
 * <p/>
 * indirizzo ip (4 byte) | subnetmask (4 byte) | gateway ip (4 byte) | lenghezza
 * SSID (1 byte) = 1 | lunghezza password (1 byte) = 1 | SSID = 0 | password =
 * 0.
 * <p/>
 * Al contrario se dovessero esserci solo le informazioni del wifi, 0,0,0,0 (4
 * byte) | 0,0,0,0 (4 byte) | 0,0,0,0 (4 byte) | lunghezza SSID (1 byte) |
 * lunghezza password (1 byte) | SSID | password.
 * <p/>
 * Souliss si aspetta una lunghezza fissa per i primi 4 parametri (4+4+4 byte)
 * ed una variabile per gli ultimi due parametri, con un minimo di 1 byte di
 * lunghezza e valore zero per entrambi.
 *
 * @author Del Pex
 */
public class BroadcastSettingsFragment extends PreferenceFragmentCompat {

    private SoulissPreferenceHelper opzioni;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        opzioni = SoulissApp.getOpzioni();

        super.onCreate(savedInstanceState);

        final EditTextPreference bcast_IP = findPreference("bcast_IP");
        final EditTextPreference bcast_subnet = findPreference("bcast_subnet");
        final EditTextPreference bcast_gateway = findPreference("bcast_gateway");
        final CheckBoxPreference bcast_isGateway = findPreference("bcast_isgateway");
        final CheckBoxPreference bcast_useDhcp = findPreference("bcast_useDhcp");
        final EditTextPreference bcast_ssid = findPreference("bcast_ssid");
        final EditTextPreference bcast_passwd = findPreference("bcast_passwd");
        final Preference sndBcast = findPreference("sndBcast");

        if (bcast_subnet.getText() == null || bcast_subnet.getText().length() == 0)
            bcast_subnet.setText(NetUtils.getDeviceSubnetMaskString(getActivity()));
        bcast_subnet.setSummary(getString(R.string.bridge_subnet) + ": " + bcast_subnet.getText());
        if (bcast_gateway.getText() == null || bcast_gateway.getText().length() == 0)
            bcast_gateway.setText(NetUtils.getDeviceGatewayString(getActivity()));
        bcast_gateway.setSummary(getString(R.string.bridge_gateway) + ": " + bcast_gateway.getText());

        sndBcast.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                final List<Byte> bcastPayload = new ArrayList<>();
                final List<Byte> ssidPayload = new ArrayList<>();
                final List<Byte> passPayload = new ArrayList<>();

                try {
                    if (bcast_IP.getText().length() == 0)
                        throw new Exception("empty parameter: IP address");
                    InetAddress i4 = InetAddress.getByName(bcast_IP.getText());
                    Log.d(it.angelic.soulissclient.Constants.TAG, "parsed bcast_IP parameter:" + i4.getHostAddress());
                    for (int tper = 0; tper < i4.getAddress().length; tper++) {
                        bcastPayload.add(i4.getAddress()[tper]);
                    }
                } catch (Exception e) {
                    bcastPayload.add((byte) 0);
                    bcastPayload.add((byte) 0);
                    bcastPayload.add((byte) 0);
                    bcastPayload.add((byte) 0);
                }
                // SUBNET
                try {
                    InetAddress is4 = InetAddress.getByName(bcast_subnet.getText());
                    Log.d(it.angelic.soulissclient.Constants.TAG, "parsed bcast_subnet parameter:" + is4.getHostAddress());
                    for (int tper = 0; tper < is4.getAddress().length; tper++) {
                        bcastPayload.add(is4.getAddress()[tper]);
                    }
                } catch (Exception e) {
                    bcastPayload.add((byte) 0);
                    bcastPayload.add((byte) 0);
                    bcastPayload.add((byte) 0);
                    bcastPayload.add((byte) 0);
                }
                // GATEWAY
                try {
                    InetAddress ig4 = InetAddress.getByName(bcast_gateway.getText());
                    for (int tper = 0; tper < ig4.getAddress().length; tper++) {
                        bcastPayload.add(ig4.getAddress()[tper]);
                    }
                    Log.d(it.angelic.soulissclient.Constants.TAG, "parsed bcast_gateway parameter:" + ig4.getHostAddress());
                } catch (Exception e) {
                    bcastPayload.add((byte) 0);
                    bcastPayload.add((byte) 0);
                    bcastPayload.add((byte) 0);
                    bcastPayload.add((byte) 0);
                }
                byte[] wifi = {0x0};
                try {
                    wifi = bcast_ssid.getText().getBytes();
                    Log.d(it.angelic.soulissclient.Constants.TAG, "parsed bcast_ssid parameter:" + bcast_ssid.getText());
                    int i = 0;
                    while (i < wifi.length) {
                        byte b = wifi[i];
                        ssidPayload.add(b);// lunghezza
                        i++;
                    }
                } catch (Exception e) {
                    Log.e(it.angelic.soulissclient.Constants.TAG, "ERROR bcast_ssid parameter:" + bcast_ssid.getText() + e.getMessage());
                }
                byte[] pass = {0x0};
                try {
                    pass = bcast_passwd.getText().getBytes();
                    Log.d(it.angelic.soulissclient.Constants.TAG, "parsed bcast_passwd parameter:" + bcast_passwd.getText());
                    for (byte b : pass) {
                        passPayload.add(b);// lunghezza
                    }
                } catch (Exception e) {
                    Log.e(it.angelic.soulissclient.Constants.TAG, "ERROR bcast_passwd parameter:" + bcast_passwd.getText() + e.getMessage());
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UDPHelper.issueBroadcastConfigure(opzioni, Constants.Net.Souliss_UDP_function_broadcast_configure, bcastPayload, bcast_isGateway.isChecked(), bcast_useDhcp.isChecked());
                        Log.w(it.angelic.soulissclient.Constants.TAG, "Souliss_UDP_function_broadcast_configure sent");
                        if (ssidPayload.size() > 0) {
                            UDPHelper.issueBroadcastConfigure(opzioni, Constants.Net.Souliss_UDP_function_broadcast_configure_wifissid, ssidPayload, null, null);
                            Log.w(it.angelic.soulissclient.Constants.TAG, "Souliss_UDP_function_broadcast_configure_wifissid sent");
                        }
                        if (passPayload.size() > 0) {
                            UDPHelper.issueBroadcastConfigure(opzioni, Constants.Net.Souliss_UDP_function_broadcast_configure_wifipass, passPayload, null, null);
                            Log.w(it.angelic.soulissclient.Constants.TAG, "Souliss_UDP_function_broadcast_configure_wifipass sent");
                        }
                        final StringBuilder textOut = new StringBuilder();
                        int numSent= passPayload.size() + ssidPayload.size() + bcastPayload.size();
                        textOut.append(SoulissApp.getAppContext().getResources().getQuantityString(R.plurals.Bytes,numSent,numSent )).append(" ");
                        textOut.append(SoulissApp.getAppContext().getString(R.string.command_sent));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(SoulissApp.getAppContext(), textOut.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();

                return true;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_broadcast_new);
    }
}