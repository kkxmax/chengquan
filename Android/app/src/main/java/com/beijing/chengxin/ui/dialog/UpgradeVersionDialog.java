package com.beijing.chengxin.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beijing.chengxin.R;

public class UpgradeVersionDialog extends Dialog {
	Context mContext;

	public interface OnUpgradeVersionClickListener {
		void onUpgradeVersion();
	}

	private OnUpgradeVersionClickListener listener;

	public void setOnUpgradeVersionClickListener(OnUpgradeVersionClickListener listener) {
		this.listener = listener;
	}

	public UpgradeVersionDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inf = LayoutInflater.from(mContext);
		View dlg = inf.inflate(R.layout.dialog_upgrade_version, (ViewGroup) findViewById(R.id.dialog_upgrade_version));
		setContentView(dlg);

		setCancelable(false);

		(findViewById(R.id.btn_notify_again)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UpgradeVersionDialog.this.cancel();
			}
		});

		(findViewById(R.id.btn_upgrade)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (listener != null)
					listener.onUpgradeVersion();
			}
		});
	}

}