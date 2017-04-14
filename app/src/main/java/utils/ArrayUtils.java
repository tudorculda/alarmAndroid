package utils;

import static utils.FFT.calcFFT;
import static utils.FFT.calcIFFT;
import static utils.FFT.calcFFT;
import static utils.FFT.calcIFFT;
import static utils.FFT.calcFFT;
import static utils.FFT.calcIFFT;
import static utils.FFT.calcFFT;
import static utils.FFT.calcIFFT;

public class ArrayUtils
{
 public static float[] crossCorrelation( short[] x, short[] y )
 {
     int tSize = x.length + y.length;
     ComplexNumber[] X = calcFFT( x , tSize );
     ComplexNumber[] Y = calcFFT( y , tSize );
     ComplexNumber[] COR = ComplexNumber.mutliply_array( X, ComplexNumber.conjugateArray( Y ) );
     ComplexNumber[] cor = calcIFFT( COR );

     float[] real = new float[cor.length ];
     for (int k=0; k< cor.length; k++)
         real[k] = cor[k].real;
     
     return real;
 }
}
