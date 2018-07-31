package dev.kxxcn.app_squad.ui.main.setting.version;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import dev.kxxcn.app_squad.data.DataRepository;

import static dev.kxxcn.app_squad.util.Constants.PLAY_STORE;
import static dev.kxxcn.app_squad.util.Constants.SEPARATOR;

/**
 * Created by kxxcn on 2018-07-31.
 */
public class VersionPresenter implements VersionContract.Presenter {

	private VersionContract.View mVersionView;
	private DataRepository mDataRepository;

	public VersionPresenter(VersionContract.View versionView, DataRepository dataRepository) {
		this.mVersionView = versionView;
		this.mDataRepository = dataRepository;
		mVersionView.setPresenter(this);
	}

	@Override
	public void onCheckVersion(final String packageName) {
		new Thread() {
			@Override
			public void run() {
				if (mVersionView == null) {
					return;
				}
				try {
					Document doc = Jsoup.connect(PLAY_STORE + packageName).get();

					Elements Version = doc.select(SEPARATOR).eq(7);

					for (Element mElement : Version) {
						mVersionView.showSuccessfulyCheckVersion(mElement.text().trim());
					}
				} catch (IOException ex) {
					mVersionView.showUnsuccessfulyCheckVersion();
					ex.printStackTrace();
				}
			}
		}.start();
	}

}
