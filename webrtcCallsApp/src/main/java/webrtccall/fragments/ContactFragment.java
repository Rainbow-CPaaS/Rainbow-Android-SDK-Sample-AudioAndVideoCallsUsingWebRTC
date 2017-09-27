package webrtccall.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.rainbowsdk.RainbowSdk;

import webrtccall.activities.StartupActivity;
import webrtccall.callapplication.R;

public class ContactFragment extends Fragment {

    private IRainbowContact m_contact;

    private StartupActivity m_activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.contact_fragment, container, false);

        if (m_contact == null) {
            throw new IllegalStateException();
        }

        if (m_activity.getSupportActionBar() != null) {
            m_activity.getSupportActionBar().setTitle(m_contact.getLastName() + " " + m_contact.getFirstName());
        }

        ImageView m_imageViewPhoto = (ImageView)fragmentView.findViewById(R.id.photo_image_view);
        if (m_contact.getPhoto() == null) {
            m_imageViewPhoto.setImageResource(R.drawable.contact);
        } else {
            m_imageViewPhoto.setImageBitmap(m_contact.getPhoto());
        }

        ImageView imageViewCallAudio = (ImageView)fragmentView.findViewById(R.id.call_audio_button);
        imageViewCallAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || m_activity.hasMicrophonePermission()) {
                    RainbowSdk.instance().webRTC().makeCall((Contact)m_contact, false);
                } else {
                    ActivityCompat.requestPermissions(m_activity, new String[] {Manifest.permission.RECORD_AUDIO}, StartupActivity.REQUEST_MAKE_AUDIO_CALL);
                }
            }
        });


        ImageView imageViewCallVideo = (ImageView)fragmentView.findViewById(R.id.call_video_button);
        imageViewCallVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasMicrophonePermission = m_activity.hasMicrophonePermission();
                boolean hasCameraPermission = m_activity.hasCameraPermission();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || hasMicrophonePermission && hasCameraPermission) {
                    RainbowSdk.instance().webRTC().makeCall((Contact) m_contact, true);
                } else {
                    if (!hasMicrophonePermission && !hasCameraPermission) {
                        ActivityCompat.requestPermissions(m_activity, new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, StartupActivity.REQUEST_MAKE_VIDEO_CALL);
                    } else if (!hasMicrophonePermission){
                        ActivityCompat.requestPermissions(m_activity, new String[] {Manifest.permission.RECORD_AUDIO}, StartupActivity.REQUEST_MAKE_VIDEO_CALL);
                    } else {
                        ActivityCompat.requestPermissions(m_activity, new String[] {Manifest.permission.CAMERA}, StartupActivity.REQUEST_MAKE_VIDEO_CALL);
                    }
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof StartupActivity){
            m_activity = (StartupActivity) context;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (activity instanceof  StartupActivity) {
                m_activity = (StartupActivity)activity;
            }
        }
    }

    public void setContact(IRainbowContact contact) {
        m_contact = contact;
    }
}
