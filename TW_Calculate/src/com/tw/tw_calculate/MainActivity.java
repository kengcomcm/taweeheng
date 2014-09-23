package com.tw.tw_calculate;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.R.integer;
import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.view.View.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

	EditText editTextLoan, editTextInterest, editTextInstallmentAmount, editTextPeriod,editTextAge, editTwInterest,editInstallmentDiscountAmount;
	Button buttonCalculate, buttonCalInsurance, buttonReset;
	RadioButton radioYear, radioMonth, radioMale,radioFemale;
	RadioGroup radioGroup,radioGroupGender;
	TextView txb3,lbViewRate,lbInsuranceAmount,lbNewInstallmentAmount,lbDiscount;
	CheckBox checkBL,checkDiscount;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        editTextLoan = (EditText) findViewById(R.id.txbLoan);
        editTextInterest = (EditText) findViewById(R.id.txbInterest);
        editTextPeriod = (EditText) findViewById(R.id.txbInstallmentPeriod);
        editTextInstallmentAmount = (EditText) findViewById(R.id.txbInstallmentAmount);
        radioMonth = (RadioButton) findViewById(R.id.rdMonth);
        radioYear = (RadioButton) findViewById(R.id.rdYear);
        buttonCalculate = (Button) findViewById(R.id.btnCalculate);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        txb3 = (TextView) findViewById(R.id.textView3);
        radioGroupGender = (RadioGroup)findViewById(R.id.radioGroup2);
        lbViewRate = (TextView) findViewById(R.id.labelRate);
        lbInsuranceAmount = (TextView) findViewById(R.id.labelInsuranceAmount);
        lbNewInstallmentAmount = (TextView) findViewById(R.id.labelNewInstallmentAmount);
        buttonCalInsurance = (Button) findViewById(R.id.btnCalInsurance);
        radioMale = (RadioButton)findViewById(R.id.rdMale);
        radioFemale = (RadioButton)findViewById(R.id.rdFemale);
        editTextAge = (EditText) findViewById(R.id.txbAge);
        checkBL = (CheckBox) findViewById(R.id.checkBL);
        checkDiscount = (CheckBox) findViewById(R.id.checkDiscount);
        editTwInterest = (EditText) findViewById(R.id.txbTWInterest);
        editInstallmentDiscountAmount =  (EditText) findViewById(R.id.txbInstallmentDiscountAmount);
        lbDiscount = (TextView) findViewById(R.id.labelDiscountAmount);
        buttonReset = (Button) findViewById(R.id.btnReset);
        
        editTextLoan.setSelectAllOnFocus(true);
        editTextInterest.setSelectAllOnFocus(true);
        editTextPeriod.setSelectAllOnFocus(true);
        editTextInstallmentAmount.setSelectAllOnFocus(true);
        editTextAge.setSelectAllOnFocus(true);
        editTwInterest.setSelectAllOnFocus(true);
        editInstallmentDiscountAmount.setSelectAllOnFocus(true);
        
        editTextInstallmentAmount.addTextChangedListener(new addListenerOnTextChange(this, editTextInstallmentAmount));
        buttonReset.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		try {
        			checkDiscount.setChecked(false);
            		checkBL.setChecked(false);
            		txb3.setText("0");
        			editTextLoan.setText("");
            		editTextInterest.setText("");
            		editTextPeriod.setText("");
            		editTextInstallmentAmount.setText("");
            		editTextAge.setText("");
            		editTwInterest.setText("");
            		editInstallmentDiscountAmount.setText("");            		
            		lbDiscount.setText("0");
            		
            		radioYear.setChecked(true);
            		radioMale.setChecked(true);
            		lbViewRate.setText("0");
            		lbInsuranceAmount.setText("0");
            		lbNewInstallmentAmount.setText("0");
				} catch (Exception e) {
					// TODO: handle exception
					msbox("",e.getMessage());
				}
        		
        	}
        });
        buttonCalculate.setOnClickListener(new OnClickListener(){
        	
			public void onClick(View v){
        		double loan =0;
        		String l = editTextLoan.getText().toString();
        		if(l.length() > 0)
        			loan = Double.valueOf(editTextLoan.getText().toString()).doubleValue();
        		double interest = 0;
        		if(editTextInterest.getText().toString().length() > 0)
        			interest = Double.valueOf(editTextInterest.getText().toString()).doubleValue();
        		
        		int period = 0;
        		if(editTextPeriod.getText().toString().length() > 0)
        			period=	Integer.valueOf(editTextPeriod.getText().toString()).intValue();
        		boolean isPerYear = true;
        		if(radioMonth.isChecked())
        			isPerYear = false;
        		double twInterest = 0;
        		if(editTwInterest.getText().toString().length() > 0)
        			twInterest = Double.valueOf(editTwInterest.getText().toString()).doubleValue();
        		boolean isInterestIncludeVat = true;
        		double installment = 0;
        		if(loan == 0 || interest == 0 || period == 0)
        		{
        			msbox("เตือน","ค่าที่กำหนดไม่ถูกต้อง");
        		}
        		else
        		{
        			installment = calInstallmentAmount(loan,interest,period,isPerYear,isInterestIncludeVat);
        			String ints = String.valueOf(installment);
        			editTextInstallmentAmount.setText(ints);
        		}
        		if(checkDiscount.isChecked())
        		{
	        		if(loan == 0 || twInterest == 0 || period ==0)
	        		{
	        			msbox("เตือน","ไม่สามารถคำนวณค่างวดส่วนลดได้เนื่องจากค่าที่กำหนดไม่ถูกต้อง");
	        		}
	        		else
	        		{
	        			installment = calInstallmentAmount(loan,twInterest,period,isPerYear,isInterestIncludeVat);
	        			String ints = String.valueOf(installment);
	        			editInstallmentDiscountAmount.setText(ints);
	        		}
	        		//setDiscount();
        		}
        	}
        }); 
        
        buttonCalInsurance.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		String gender = "M";
        		if(radioFemale.isChecked())
        			gender = "F";
        		int Age = 0;
        		if(editTextAge.getText().toString().length() > 0)
        			Age =Integer.valueOf(editTextAge.getText().toString()).intValue();
        		boolean isBL = checkBL.isChecked();
        		double percent = findInsuranceRate(gender, Age, isBL);
        		double loan =0;
        		if(editTextLoan.getText().toString().length() > 0)
        			loan = Double.valueOf(editTextLoan.getText().toString()).doubleValue();
        		double period = 0;
        		if(editTextPeriod.getText().toString().length() > 0)
        			period=	Double.valueOf(editTextPeriod.getText().toString()).intValue();
        		double interest = 0;
        		if(txb3.getText().toString().length() > 0)
        			interest = Double.valueOf(txb3.getText().toString()).doubleValue();
        		if(percent == 0)
        			msbox("เตือน","อายุที่ระบุไม่สามารถทำประกันได้");
        		else if(loan == 0 || interest == 0 || period == 0)
        		{
        			msbox("เตือน","ค่าที่กำหนดไม่ถูกต้องกรุณาตรวจสอบ");
        		}
        		else
        		{
        			double yearperiod = Math.ceil(period / 12);
        			percent = percent * yearperiod;
        			
        			
                    double premium = 0;
                    double assetIncludeVatAmount = (loan * 1.07);
                    
                    double premiumpercent = (percent / 100);
                    
                    premium = (((assetIncludeVatAmount * (interest / 100)) * yearperiod) + assetIncludeVatAmount) * premiumpercent;                    
                    premium =Math.round(premium);
                    
                    
                    loan = loan +premium;
                    double newInstallmentAmount =  calInstallmentAmount(loan, interest, Integer.valueOf(editTextPeriod.getText().toString()).intValue(), radioYear.isChecked(), false);
                    String strInstallment = "";//String.valueOf();
                    String strPremium = "";
                    
                    NumberFormat numberFormat  = new DecimalFormat("#,###,###");
                    numberFormat = new DecimalFormat("#,###,###");
                    strInstallment = numberFormat.format(Math.round(newInstallmentAmount));      // -1 234 568
                    strPremium = numberFormat.format(Math.round(premium));
            		
                    NumberFormat formatter = new DecimalFormat("0.00");
                    String srtPercent = formatter.format(percent);                   

                    
                    lbViewRate.setText(srtPercent);
            		lbInsuranceAmount.setText(strPremium);
            		lbNewInstallmentAmount.setText(strInstallment);
        		}
        	}        	
        }); 
        	
        editInstallmentDiscountAmount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub	
				if(checkDiscount.isChecked())
					setDiscount();
			}
			
		});
        	
        
    }
    
    public void setDiscount()
    {
    	double discount = 0;
		double installmentAmount =0;
		double installmentDiscountAmount =0;
		if(editTextInstallmentAmount.getText().toString().length() > 0)
			installmentAmount = Double.valueOf(editTextInstallmentAmount.getText().toString()).doubleValue();
		if(editInstallmentDiscountAmount.getText().toString().length() > 0)
			installmentDiscountAmount = Double.valueOf(editInstallmentDiscountAmount.getText().toString()).doubleValue();
		
		discount = installmentAmount - installmentDiscountAmount;
		NumberFormat numberFormat  = new DecimalFormat("#,###,###");
        String strDiscount = numberFormat.format(Math.round(discount));      // -1 234 568                
        	    		
		lbDiscount.setText(strDiscount);
    }
    
    public class addListenerOnTextChange implements TextWatcher {private Context mContext; 	EditText mEdittextview;

    	public addListenerOnTextChange(Context context, EditText edittextview) {
    	    super();
    	    this.mContext = context;
    	    this.mEdittextview= edittextview;
    	}
    	@Override
    	public void afterTextChanged(Editable s) {  
    		double result = 0;
    		double installmentAmount =0;
    		if(editTextInstallmentAmount.getText().toString().length() > 0)
    			installmentAmount = Double.valueOf(editTextInstallmentAmount.getText().toString()).doubleValue();
    		int period = 0;
    		if(editTextPeriod.getText().toString().length() > 0)
    			period=	Integer.valueOf(editTextPeriod.getText().toString()).intValue();
    		double loan =0;
    		if(editTextLoan.getText().toString().length() > 0)
    			loan = Double.valueOf(editTextLoan.getText().toString()).doubleValue();
    		boolean isPerYear = true;
    		if(radioMonth.isChecked())
    			isPerYear = false;
    		if(installmentAmount == 0 || loan == 0 || period == 0)
    		{
    			//msbox("เตือน","ค่าที่กำหนดไม่ถูกต้อง");
    		}
    		else
    		{
    			result = find_Interest_Exclude_Vat(installmentAmount,period,loan,isPerYear);
    			String ints = String.valueOf(result);
        		int dot = ints.lastIndexOf('.');
        		String x = ints.substring(0,dot+3);
        		txb3.setText(x);
        		setDiscount();
    		}
    		
    	}

    	@Override
    	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    	}

    	@Override
    	public void onTextChanged(CharSequence s, int start, int before, int count) {
    	    //What you want to do
    	}
    }
    
    public void msbox(String str,String str2)
    {
    	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
        dlgAlert.setTitle(str); 
        dlgAlert.setMessage(str2); 
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                 //finish(); 
            }
       });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public double calInstallmentAmount(double LoanAmount, double Interest, int installmentPriod, boolean isPerYear, boolean isInterestIncludeVat)
    {
    	double result = 0;
        double x = 0;
        if (isPerYear)
        {
            //ค่างวด (ดอกเบี้ยเต็ม) = ((เงินต้น x ดอกเบี้ย x จำนวนปี) + เงินต้น) / จำนวนงวด
        	
            double year = Double.valueOf(installmentPriod).doubleValue() / 12;
            x = LoanAmount * (Interest / 100);
            x = x * year;
            x = x + LoanAmount;
            if (!isInterestIncludeVat)//ดอกเบี้ยรวม vat แล้วหรือไม่
            {
                x = x + (x * 7 / 100);
            }
            x = x / installmentPriod;
            result = x;
        }
        else
        {
            //ค่างวด (ดอกเบี้ยเต็ม) = ((เงินต้น x ดอกเบี้ย x จำนวนงวด) + เงินต้น) / จำนวนงวด
            x = LoanAmount * ((Interest / 100));
            x = x * installmentPriod;
            if (isPerYear)
                x = x + LoanAmount;
            if (!isInterestIncludeVat)//ดอกเบี้ยรวม vat แล้วหรือไม่
            {
                x = x + (x * 7 / 100);
            }
            x = x / installmentPriod;
            result = x;
        }
        result = Math.round(result);
        return result;
    }
    
    public double find_Interest_Exclude_Vat(double Installment_Amount, int Installment_Period, double Principle, boolean isInterest_Per_Year)
    {
        double result = 0;
        
        if (isInterest_Per_Year)
        {
            double year = Double.valueOf(Installment_Period).doubleValue() / 12;
            result = ((((Installment_Amount * Installment_Period * 100) / 107) - Principle) / year * 100) / Principle;
        }
        else
        {
            result = ((((Installment_Amount * Installment_Period * 100) / 107) - Principle) / Installment_Period * 100) / Principle;
        }

        
       /* DecimalFormat nf = new DecimalFormat("00.00");
        nf.setRoundingMode(RoundingMode.DOWN);
        String formatted = nf.format(result);*/
        /*NumberFormat f = NumberFormat.getInstance("@@##");
        
        result = double.Parse(string.Format("{0:#,#.000}", result));
        string result1 = result.ToString().PadLeft(6, '0').Substring(0, 5);
        double.TryParse(result1, out result);*/
        //return Double.valueOf(nf.format(result)).doubleValue();
        return result;
    }
    public double findInsuranceRate(String gender,int Age,boolean isBL)
    {
    	double percent = 0;
        if (isBL)
        {
            percent = 0.27;
        }
        else
        {
            if (gender == "M")
            {
                if (Age >= 16 && Age <= 20)
                {
                    percent = 0.38;
                }
                else if (Age >= 21 && Age <= 30)
                {
                    percent = 0.43;
                }
                else if (Age >= 31 && Age <= 40)
                {
                    percent = 0.49;
                }
                else if (Age >= 41 && Age <= 45)
                {
                    percent = 0.56;
                }
                else if (Age >= 46 && Age <= 50)
                {
                    percent = 0.73;
                }
                else if (Age >= 51 && Age <= 55)
                {
                    percent = 0.85;
                }
                else if (Age >= 56 && Age <= 60)
                {
                    percent = 1.28;
                }
                else if (Age >= 61 && Age <= 65)
                {
                    percent = 1.99;
                }
            }
            else if (gender == "F")
            {
                if (Age >= 16 && Age <= 20)
                {
                    percent = 0.27;
                }
                else if (Age >= 21 && Age <= 30)
                {
                    percent = 0.30;
                }
                else if (Age >= 31 && Age <= 40)
                {
                    percent = 0.32;
                }
                else if (Age >= 41 && Age <= 45)
                {
                    percent = 0.38;
                }
                else if (Age >= 46 && Age <= 50)
                {
                    percent = 0.46;
                }
                else if (Age >= 51 && Age <= 55)
                {
                    percent = 0.63;
                }
                else if (Age >= 56 && Age <= 60)
                {
                    percent = 0.89;
                }
                else if (Age >= 61 && Age <= 65)
                {
                    percent = 1.26;
                }
            }
        }
        return percent;
    }
    
}
