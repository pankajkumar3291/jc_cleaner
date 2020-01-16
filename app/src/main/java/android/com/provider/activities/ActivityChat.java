
package android.com.provider.activities;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import java.util.ArrayList;
import java.util.List;

public class ActivityChat extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<FirebaseMessage> messageList = new ArrayList<>();
    private RecyclerView recChat;
    private ImageView btnSend, imgBack;
    private EditText etmessage;
    private String proId;
    String jobid;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recChat = findViewById(R.id.rec_chat);
        btnSend = findViewById(R.id.imageView2);
        etmessage = findViewById(R.id.editText19);
        imgBack = findViewById(R.id.backarrr);
        imgBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!TextUtils.isEmpty(etmessage.getText().toString()) && !TextUtils.isEmpty(proId) && !TextUtils.isEmpty(jobid)) {
                    FirebaseMessage message = new FirebaseMessage();
                    message.setMessage(etmessage.getText().toString());
                    message.setId(Long.parseLong(proId));
                    DatabaseReference newRef = mDatabase.child(jobid).push();
                    newRef.setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            etmessage.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
        });
        if (getIntent() != null) {
            if (getIntent().hasExtra("jobid")) {
                jobid = String.valueOf(getIntent().getIntExtra("jobid", 0));
                proId = String.valueOf(Hawk.get("savedUserId"));
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child(jobid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        messageList.clear();

                        if(dataSnapshot.getValue()!=null)
                        {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                FirebaseMessage user = dataSnapshot1.getValue(FirebaseMessage.class);
                                messageList.add(user);
                            }
                            recChat.setHasFixedSize(true);
                            recChat.setAdapter(new ChatAdapter(messageList, ActivityChat.this));
                            recChat.smoothScrollToPosition(messageList.size() - 1);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError){
                    }
                });
            }
        }
    }

    //todo                       chatAdapter
    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
        private List<FirebaseMessage> chatAdapterList;
        private Context context;
        public ChatAdapter(List<FirebaseMessage> chatAdapterList, Context context) {
            this.chatAdapterList = chatAdapterList;
            this.context = context;
        }
        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_adapter, viewGroup, false);
            return new ChatViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
            FirebaseMessage firebaseMessage = chatAdapterList.get(i);
            String id = String.valueOf(Hawk.get("savedUserId"));
            if (firebaseMessage.getId() == Integer.valueOf(id)) {
                chatViewHolder.tvReciever.setText(firebaseMessage.getMessage());
                chatViewHolder.tvReciever.setVisibility(View.VISIBLE);
                chatViewHolder.tvSenddr.setVisibility(View.GONE);
            } else {
                chatViewHolder.tvSenddr.setText(firebaseMessage.getMessage());
                chatViewHolder.tvSenddr.setVisibility(View.VISIBLE);
                chatViewHolder.tvReciever.setVisibility(View.GONE);
            }
        }
        @Override
        public int getItemCount() {
            return chatAdapterList.size();
        }
        public class ChatViewHolder extends RecyclerView.ViewHolder {
            private TextView tvSenddr, tvReciever;
            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);
                tvSenddr = itemView.findViewById(R.id.textView3);
                tvReciever = itemView.findViewById(R.id.textView31);
            }
        }
    }
}
