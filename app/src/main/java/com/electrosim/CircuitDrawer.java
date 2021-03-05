package com.electrosim;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.electrosim.component.AndGate;
import com.electrosim.component.Extension;
import com.electrosim.component.Ground;
import com.electrosim.component.Led;
import com.electrosim.component.NandGate;
import com.electrosim.component.NorGate;
import com.electrosim.component.NotGate;
import com.electrosim.component.OrGate;
import com.electrosim.component.Vcc;
import com.electrosim.component.XnorGate;
import com.electrosim.component.XorGate;
import com.electrosim.simulation.SimConnectorFactory;
import com.electrosim.simulation.SimContainer;

public class CircuitDrawer extends Activity implements OnClickListener {

	Map<Integer, ElementInterface> btElementMap = new HashMap<Integer, ElementInterface>();

	Container container;
	boolean simtoggle = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		container = (Container) this.findViewById(R.id.surfaceView1);
		container.setConnectorFactory(new SimConnectorFactory());
		Button ext = (Button) findViewById(R.id.ext);
		ext.setOnClickListener(this);
		Button and = (Button) findViewById(R.id.and);
		and.setOnClickListener(this);
		Button nand = (Button) findViewById(R.id.nand);
		nand.setOnClickListener(this);
		Button or = (Button) findViewById(R.id.or);
		or.setOnClickListener(this);
		Button nor = (Button) findViewById(R.id.nor);
		nor.setOnClickListener(this);
		Button xor = (Button) findViewById(R.id.xor);
		xor.setOnClickListener(this);
		Button xnor = (Button) findViewById(R.id.xnor);
		xnor.setOnClickListener(this);
		Button not = (Button) findViewById(R.id.not);
		not.setOnClickListener(this);
		Button led = (Button) findViewById(R.id.led);
		led.setOnClickListener(this);
		Button vcc = (Button) findViewById(R.id.vcc);
		vcc.setOnClickListener(this);
		Button gnd = (Button) findViewById(R.id.gnd);
		gnd.setOnClickListener(this);
		Button and4 = (Button) findViewById(R.id.and4);
		and4.setOnClickListener(this);
		Button nand4 = (Button) findViewById(R.id.nand4);
		nand4.setOnClickListener(this);
		Button or4 = (Button) findViewById(R.id.or4);
		or4.setOnClickListener(this);
		Button nor4 = (Button) findViewById(R.id.nor4);
		nor4.setOnClickListener(this);
		Button xor4 = (Button) findViewById(R.id.xor4);
		xor4.setOnClickListener(this);
		Button xnor4 = (Button) findViewById(R.id.xnor4);
		xnor4.setOnClickListener(this);
		// Button ext = (Button) findViewById(R.id.ext);
		// ext.setOnClickListener(this);
		Button sim = (Button) findViewById(R.id.sim);
		sim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				if (((SimContainer) container).isRunning())
				{
					((SimContainer) container).stop();
					v.setBackgroundResource(R.drawable.play);
				}
			
			   else
				{
					((SimContainer) container).start();
					v.setBackgroundResource(R.drawable.pause);
				}
			}
		});
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			showDiag();
			}
			});
		
		Button del = (Button) findViewById(R.id.del);
		del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				container.setDelflag(!(container.isDelflag()));
				
			}
		});
		Resources resources = getResources();

		btElementMap.put(R.id.ext, new Extension(20, 220, container, 1,resources));

		btElementMap.put(R.id.and,new AndGate(20, 220, container, 2, resources));
		btElementMap.put(R.id.and4, new AndGate(20, 220, container, 4,resources));

		btElementMap.put(R.id.nand, new NandGate(20, 220, container, 2,
				resources));

		btElementMap.put(R.id.nand4, new NandGate(20, 220, container, 4,
				resources));

		btElementMap.put(R.id.or, new OrGate(20, 220, container, 2, resources));
		btElementMap.put(R.id.or4, new OrGate(20, 220, container, 4, resources));

		btElementMap.put(R.id.xor,new XorGate(20, 220, container, 2, resources));
		btElementMap.put(R.id.xor4, new XorGate(20, 220, container, 4,resources));

		btElementMap.put(R.id.nor,new NorGate(20, 220, container, 2, resources));
		btElementMap.put(R.id.nor4, new NorGate(20, 220, container, 4,resources));

		btElementMap.put(R.id.xnor, new XnorGate(20, 220, container, 2,
				resources));
		btElementMap.put(R.id.xnor4, new XnorGate(20, 220, container, 4,
				resources));
		btElementMap.put(R.id.not,new NotGate(20, 220, container, 0, resources));
		btElementMap.put(R.id.led, new Led(20, 220, container, 1, resources));
		btElementMap.put(R.id.vcc, new Vcc(20, 220, container, 0, resources));
		btElementMap
				.put(R.id.gnd, new Ground(20, 220, container, 0, resources));

		// btElementMap.put(R.id.and, new AndGate(20, 220, container,
		// 2,resources));
		// btElementMap.put(R.id.and, new AndGate(20, 220, container,
		// 2,resources));
	}

	protected void showDiag() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Save Circuit");
		alert.setMessage("Circuit Name:");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  Editable value = input.getText();
		  // Do something with value!
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}

	@Override
	public void onClick(View button) {

		ElementInterface element = btElementMap.get(button.getId());
		if (element != null)
			container.insertElement(element.createNew());
		else
			Log.d("ss", "image not found");
	}
}