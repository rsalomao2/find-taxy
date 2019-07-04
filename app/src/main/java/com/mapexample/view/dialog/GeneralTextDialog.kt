package com.mapexample.view.dialog

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.mapexample.R
import kotlinx.android.synthetic.main.lay_dialog_text.*

class GeneralTextDialog : DialogFragment() {

    private var title: String? = null
    private var detail: String? = null
    private var mView: View? = null
    private var mListener: DialogMsgInterface? = null

    interface DialogMsgInterface {
        fun onDialogMsgComplete()
    }

    companion object {
        private const val TITLE = "title"
        private const val DETAIL = "detail"


        fun newInstance(title: String, detail: String): GeneralTextDialog {
            val frag = GeneralTextDialog()
            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(DETAIL, detail)

            frag.arguments = args
            return frag
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            title = arguments!!.getString(TITLE)
            detail = arguments!!.getString(DETAIL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.lay_dialog_text, container)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setInfo()
        setActions()
    }

    override fun onResume() {
        //Force Fragment Dialog to have wider size
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as WindowManager.LayoutParams
        super.onResume()
    }

    private fun setInfo() {

        (mView!!.findViewById(R.id.tvi_title) as TextView).text = title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            (mView!!.findViewById(R.id.tvi_message) as TextView).text =
                    Html.fromHtml(detail, Html.FROM_HTML_MODE_COMPACT)
        } else {
            (mView!!.findViewById(R.id.tvi_message) as TextView).text = detail
            (mView!!.findViewById(R.id.tvi_message) as TextView).text = Html.fromHtml(detail)
        }

    }

    private fun setActions() {
        btnAction.setOnClickListener {
            if (mListener != null) {
                dismiss()
                mListener!!.onDialogMsgComplete()
            }
        }

    }

    fun setOnDialogMsgInterface(mListener: DialogMsgInterface) {
        this.mListener = mListener
    }


}
