package utils;

public class FFT
{

    public ComplexNumber[] rez;
    public int xSize;
    private final int[] puteri_doi =
    {
        0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152
    };
    private final float[] W_real, W_imag;
    public float aux;
    public int k;

    private FFT( float[] data, int size )
    { // constructor
        float[] x_inReal;
        float[] x_inImag;
        int index = 0;
        while ( size > puteri_doi[index] )
        {
            index++;
        }
        x_inReal = new float[puteri_doi[index]];
        x_inImag = new float[puteri_doi[index]];
        xSize = x_inReal.length;
        for ( int i = 0; i < data.length; i++ )
        {
            x_inReal[i] = data[i];
            x_inImag[i] = 0;
        }
        for ( int i = data.length; i < x_inReal.length; i++ )
        {
            x_inReal[i] = 0;
            x_inImag[i] = 0;
        }

        W_real = new float[puteri_doi[index - 1]];
        W_imag = new float[puteri_doi[index - 1]];
        for ( int i = 0; i < W_real.length; i++ )
        {
            W_real[i] = (float)Math.cos( ( -2 ) * Math.PI * i / x_inReal.length );
            W_imag[i] = (float)Math.sin( ( -2 ) * Math.PI * i / x_inReal.length );
//            			System.out.print("W_real "+W_real[i]+"\n");
//			System.out.print("W_imag "+W_imag[i]+"\n");
        }

        rez = fftCalc( x_inReal, x_inImag );
    }// end constructor

    private FFT( ComplexNumber[] data, int size )
    { // constructor
        float[] x_inReal;
        float[] x_inImag;
        int index = 0;
        while ( size > puteri_doi[index] )
        {
            index++;
        }
        x_inReal = new float[puteri_doi[index]];
        x_inImag = new float[puteri_doi[index]];
        xSize = x_inReal.length;
        for ( int i = 0; i < data.length; i++ )
        {
            x_inReal[i] = data[i].real;
            x_inImag[i] = data[i].imag;

        }
        for ( int i = data.length; i < x_inReal.length; i++ )
        {
            x_inReal[i] = 0;
            x_inImag[i] = 0;
        }

        W_real = new float[puteri_doi[index - 1]];
        W_imag = new float[puteri_doi[index - 1]];
        for ( int i = 0; i < W_real.length; i++ )
        {
            W_real[i] = (float)Math.cos( ( -2 ) * Math.PI * i / x_inReal.length );
            W_imag[i] = (float)Math.sin( ( -2 ) * Math.PI * i / x_inReal.length );

        }

        rez = fftCalc( x_inReal, x_inImag );
    }// end constructor

    private ComplexNumber[] fftCalc( float[] x_inReal, float[] x_inImag )
    {

        if ( x_inReal.length > 2 )
        {
            float[] x_parR, x_imparR, x_parI, x_imparI, real_rez, imag_rez;
            ComplexNumber[] x_par, x_impar, rezu;

            real_rez = new float[x_inReal.length];
            imag_rez = new float[x_inReal.length];
            x_parR = new float[x_inReal.length / 2];
            x_imparR = new float[x_inReal.length / 2];
            x_parI = new float[x_inReal.length / 2];
            x_imparI = new float[x_inReal.length / 2];
            for ( k = 0; k < x_parR.length; k++ )
            {

                x_parR[k] = x_inReal[2 * k];
                x_imparR[k] = x_inReal[2 * k + 1];
                x_parI[k] = x_inImag[2 * k];
                x_imparI[k] = x_inImag[2 * k + 1];
            }
            int inmult = ( xSize / x_parR.length ) / 2;
            x_par = fftCalc( x_parR, x_parI );
            x_impar = fftCalc( x_imparR, x_imparI );
            for ( k = 0; k < x_parR.length; k++ )
            {
                aux = W_real[inmult * k] * x_impar[k].real - W_imag[inmult * k] * x_impar[k].imag;

                real_rez[k] = x_par[k].real + aux;
                real_rez[k + x_parR.length] = x_par[k].real - aux;
                aux = W_real[inmult * k] * x_impar[k].imag + W_imag[inmult * k] * x_impar[k].real;

                imag_rez[k] = x_par[k].imag + aux;

                imag_rez[k + x_parR.length] = x_par[k].imag - aux;

            }
            rezu = new ComplexNumber[real_rez.length];
            for ( int i = 0; i < rezu.length; i++ )
            {
                rezu[i] = new ComplexNumber( real_rez[i], imag_rez[i] );
            }

            return rezu;

        } else
        {
            float[] r = new float[2];
            float[] i = new float[2];
            r[0] = x_inReal[0] + x_inReal[1];
            i[0] = x_inImag[0] + x_inImag[1];
            r[1] = x_inReal[0] - x_inReal[1];
            i[1] = x_inImag[0] - x_inImag[1];
            ComplexNumber[] rezu;
            rezu = new ComplexNumber[r.length];
            for ( int k = 0; k < rezu.length; k++ )
            {
                rezu[k] = new ComplexNumber( r[k], i[k] );
            }
            return rezu;

        }

    }// end fftCalc

    public static ComplexNumber[] calcFFT( float[] x )
    {
        return calcFFT( x, x.length );

    } // end functie statica de return rezultat

    public static ComplexNumber[] calcFFT( float[] x, int size )
    {
        FFT ft = new FFT( x, size );
        return ft.rez;

    } // end functie statica de return rezultat

    public static ComplexNumber[] calcFFT( short[] x, int size )
    {
        float[] xd = new float[x.length];
        for (int i = 0; i < xd.length; i++ )
            xd[i] = x[i] / 32767.0f;
        FFT ft = new FFT( xd, size );
        return ft.rez;

    } // end functie statica de return rezultat
    public static ComplexNumber[] calcIFFT( float[] x )
    {
        FFT ft = new FFT( x, x.length );
        ComplexNumber.conjugateArray( ft.rez );
        ComplexNumber.mutliplyConstantToArray( ( (float) 1 ) / ft.rez.length, ft.rez );
        return ft.rez;

    } // end functie statica de return rezultat	

    public static ComplexNumber[] calcIFFT( ComplexNumber[] x, int size )
    {
        ComplexNumber.conjugateArray( x );
        FFT ft = new FFT( x, size );
        ComplexNumber.conjugateArray( ft.rez );
        ComplexNumber.mutliplyConstantToArray( ( (float) 1 ) / ft.rez.length, ft.rez );
        return ft.rez;

    } // end functie statica de return rezultat	

    public static ComplexNumber[] calcIFFT( ComplexNumber[] x )
    {
        return calcIFFT( x, x.length );

    } // end functie statica de return rezultat	

}
