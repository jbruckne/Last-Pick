package joebruckner.lastpick.actors;

public interface Actor<Data> {
	void showLoading();
	void showContent(Data data);
	void showError(String errorMessage);
}
