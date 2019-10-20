package it.angelic.soulissclient.preferences;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.preference.PreferenceDialogFragmentCompat;
import it.angelic.soulissclient.R;
import it.angelic.soulissclient.model.ISoulissObject;
import it.angelic.soulissclient.model.LauncherElement;
import it.angelic.soulissclient.model.SoulissModelException;
import it.angelic.soulissclient.model.SoulissNode;
import it.angelic.soulissclient.model.SoulissScene;
import it.angelic.soulissclient.model.SoulissTag;
import it.angelic.soulissclient.model.SoulissTypical;
import it.angelic.soulissclient.model.db.SoulissDBLauncherHelper;
import it.angelic.soulissclient.util.LauncherElementEnum;

/**
 * Created by shine@angelic.it on 20/10/2019.
 */
public class LauncherAddCustomDialog extends PreferenceDialogFragmentCompat {
    private static LauncherElementEnum[] statArr = new LauncherElementEnum[]{LauncherElementEnum.NODE, LauncherElementEnum.TYPICAL, LauncherElementEnum.SCENE, LauncherElementEnum.TAG};
    private Context context;
    private SoulissDBLauncherHelper datasource;
    private Spinner outputTYpSpinner;
    private Spinner typeSpinner;

    public static LauncherAddCustomDialog newInstance(String key) {
        final LauncherAddCustomDialog fragment = new LauncherAddCustomDialog();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateDialogView(final Context ctx) {
        // return super.onCreateDialogView();
        context = ctx;
        datasource = new SoulissDBLauncherHelper(context);
        View dialoglayout = View.inflate(new ContextWrapper(context), R.layout.add_to_launcher_dialog, null);
        typeSpinner = dialoglayout.findViewById(R.id.elementType);
        outputTYpSpinner = dialoglayout.findViewById(R.id.elementData);
        ArrayAdapter<LauncherElementEnum> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, statArr);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        ArrayAdapter<SoulissNode> adaptertyp = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, datasource.getAllNodes());
        adaptertyp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        outputTYpSpinner.setAdapter(adaptertyp);


        // Cambiando nodo, cambia i tipici
        AdapterView.OnItemSelectedListener lit = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (statArr[pos].equals(LauncherElementEnum.NODE)) {
                    ArrayAdapter<SoulissNode> adaptertyp = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, datasource.getAllNodes());
                    //setTypicalSpinner(outputTypicalSpinner, nodiArray[pos], context);
                    outputTYpSpinner.setAdapter(adaptertyp);
                } else if (statArr[pos].equals(LauncherElementEnum.TYPICAL)) {
                    ArrayAdapter<SoulissTypical> adaptertyp = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, datasource.getAllTypicals());
                    //setTypicalSpinner(outputTypicalSpinner, nodiArray[pos], context);
                    outputTYpSpinner.setAdapter(adaptertyp);
                } else if (statArr[pos].equals(LauncherElementEnum.TAG)) {
                    ArrayAdapter<SoulissTag> adaptertyp = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, datasource.getRootTags());
                    //setTypicalSpinner(outputTypicalSpinner, nodiArray[pos], context);
                    outputTYpSpinner.setAdapter(adaptertyp);
                } else if (statArr[pos].equals(LauncherElementEnum.SCENE)) {
                    ArrayAdapter<SoulissScene> adaptertyp = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, datasource.getScenes());
                    //setTypicalSpinner(outputTypicalSpinner, nodiArray[pos], context);
                    outputTYpSpinner.setAdapter(adaptertyp);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        typeSpinner.setOnItemSelectedListener(lit);
        return dialoglayout;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // Save user preferences
            ISoulissObject sobj = (ISoulissObject) outputTYpSpinner.getSelectedItem();
            if (sobj != null) {
                LauncherElement newEl = new LauncherElement();
                newEl.setComponentEnum(statArr[typeSpinner.getSelectedItemPosition()]);
                newEl.setTitle(statArr[typeSpinner.getSelectedItemPosition()].toString() + ": " + sobj.getNiceName());
                newEl.setLinkedObject(sobj);
                Toast.makeText(context, "Saved new Launcher item: " + newEl.getTitle(), Toast.LENGTH_SHORT).show();
                try {
                    datasource.addElement(newEl);
                } catch (SoulissModelException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Choose related data", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "Cancel was clicked", Toast.LENGTH_SHORT).show();
        }
    }

}
