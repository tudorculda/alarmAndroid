package utils;

public class ComplexNumber
{

    public float real, imag;

    public ComplexNumber( float real )
    {
        this( real, 0 );
    }

    public ComplexNumber( float real, float imag )
    {
        this.real = real;
        this.imag = imag;
    }

    public float getAbs()
    {
        return (float)Math.sqrt( real * real + imag * imag );
    }

    public float getAbs_dB()
    {
        double toOutput = getAbs();
        if ( toOutput == 0 )
        {
            toOutput = Double.MIN_NORMAL;
        }
        return 20 * (float)Math.log10( toOutput );
    }

    public ComplexNumber conjugate()
    {
        return new ComplexNumber( real, -imag );
    }

    @Override
    public String toString()
    {
        String op = imag >= 0 ? "+" : "";
        return "" + real + op + imag + "i";
    }

    public static ComplexNumber[] conjugateArray( ComplexNumber[] cArray )
    {
        for ( int i = 0; i < cArray.length; i++ )
        {
            cArray[i].imag *= -1;
        }
        return cArray;
    }

    public static void mutliplyConstantToArray( double k, ComplexNumber[] cArray )
    {
        for ( int i = 0; i < cArray.length; i++ )
        {
            cArray[i].imag *= k;
            cArray[i].real *= k;
        }
    }

    public static ComplexNumber[] mutliply_array( ComplexNumber[] x, ComplexNumber[] y )
    {
        int minSize = x.length < y.length ? x.length : y.length;
        float real, imag;
        ComplexNumber[] result = new ComplexNumber[ minSize ];
        for ( int i = 0; i < minSize; i++ )
        {
            
            real = x[i].real * y[i].real - x[i].imag * y[i].imag;
            imag = x[i].real * y[i].imag + x[i].imag * y[i].real;
            result[i] = new ComplexNumber(real, imag);

        }
        return result;
    }
}
