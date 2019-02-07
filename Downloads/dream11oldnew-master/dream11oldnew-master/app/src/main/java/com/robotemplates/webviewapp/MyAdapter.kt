package com.robotemplates.webviewapp

import android.content.Context

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.widget.Toast
import com.robotemplates.webviewapp.Posts
import com.robotemplates.webviewapp.Team
import com.robotemplates.webviewapp.Sport

class MyAdapter(fm: FragmentManager, lp:ArrayList<Posts>, ls:ArrayList<Sport>, lt:ArrayList<Team>, c: Context,lu:ArrayList<PaidUser>,pi:PaidInterface,ps:ArrayList<PaidSport>,b:String) : FragmentStatePagerAdapter(fm),PaidInterface{
    override fun savedLogin(sportid:Int) {
        paidInterface.onLogin(sportid)
    }

    override fun onLogin(sportid: Int) {
        var dup = mListOfSport;
        Toast.makeText(context,"Loading."+sportid,Toast.LENGTH_LONG).show()

        for(Sport in dup){
            Log.d("YOYO","Id"+Sport.id);
           if (sportid == Sport.id.toInt()){
                var index = mListOfSport.indexOf(Sport);
               var s = Sport;
               s.isPaid = false
               mListOfSport.set(index,s)
         //      this.notifyDataSetChanged()

               notifyDataSetChanged()
               paidInterface.onLogin(sportid)
               Toast.makeText(context,"Loading..",Toast.LENGTH_LONG).show()
           }
       }
    }

    var mListOfPosts : ArrayList<Posts> = ArrayList();
    lateinit var mListOfSport : ArrayList<Sport>;
    lateinit var mListOfTeam: ArrayList<Team>;
    lateinit var mListOfUser: ArrayList<PaidUser>;
    lateinit var mListOfPaidSport: ArrayList<PaidSport>;
    lateinit var context: Context;
    lateinit var paidInterface: PaidInterface;
    lateinit var bank : String;
    lateinit var fmm: FragmentManager;
    lateinit var mfragment: TabBaseFragment
    init{
        mListOfPosts = lp;
        mListOfSport = ls;
        mListOfPaidSport = ps;
        mListOfTeam = lt;
        context = c;
        mListOfUser = lu;
        paidInterface = pi;
        fmm = fm
        bank = b;
    }


    override fun getItem(position: Int): Fragment{
        var mList:ArrayList<Posts> = ArrayList();
        for(posts in mListOfPosts){
            if(posts.sport == mListOfSport.get(position).id.toInt()){
                mList.add(posts)
            }
        }

        var fragment = TabBaseFragment.newInstance(mList, mListOfTeam, mListOfSport.get(position).id.toInt(),context,mListOfSport.get(position).isPaid,mListOfUser,this,mListOfPaidSport,bank,mListOfSport);
        mfragment = fragment
        return fragment;
    }
    override fun getItemPosition(`object`: Any): Int {
        var frag = `object` as TabBaseFragment

        if(frag.loginpage){
            return super.getItemPosition(`object`)
        }else{
            return POSITION_NONE;
        }

    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        if(::mfragment.isInitialized){

            if(mfragment.paid){
                mfragment.loginpage=true;
            }
        }
    }
    override fun getPageTitle(position: Int): CharSequence {
       return mListOfSport.get(position).name
    }

    override fun getCount(): Int = mListOfSport.size;


}