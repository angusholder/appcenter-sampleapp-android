package ms.appcenter.sampleapp.android;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.analytics.EventProperties;

import ms.appcenter.sampleapp.android.databinding.UserRootBinding;

public class UserFragment extends Fragment {

    public UserFragment() {
    }

    static String userId = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UserRootBinding v = UserRootBinding.inflate(inflater, container, false);
        v.login.setOnClickListener((view) -> {
            new UserIdDialog().show(requireFragmentManager(), null);
        });
        v.logout.setOnClickListener((view) -> {
            AppCenter.setUserId(null);
            Analytics.trackEvent("Session", new EventProperties()
                    .set("kind", "logout")
                    .set("userId", userId));
            userId = null;
        });
        return v.getRoot();
    }

    public static class UserIdDialog extends DialogFragment {
        static String[] USER_IDS = new String[]{"abc", "def", "123"};

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return new AlertDialog.Builder(requireActivity())
                    .setTitle("Select user ID")
                    .setItems(USER_IDS, (dialog, which) -> {
                        String userId = USER_IDS[which];
                        AppCenter.setUserId(userId);
                        UserFragment.userId = userId;
                        Analytics.trackEvent("Session", new EventProperties()
                                .set("kind", "login")
                                .set("userId", userId));
                    })
                    .create();
        }
    }
}
