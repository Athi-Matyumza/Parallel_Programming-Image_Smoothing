# Parallel Programming with the Java Fork/Join framework:
## Parallel 2D Median Filter for Image Smoothing

## Overview

This project focuses on implementing parallel algorithms for smoothing RGB color images using the Java Fork-Join library. The project involves two parallel filters: a mean filter and a median filter. The mean filter computes the average of surrounding pixels, while the median filter calculates the median value. The median filter is particularly effective at preserving edges in images.

## Image Smoothing Filters

### Mean Filter
- Averages all pixels within the specified window.

### Median Filter
- Sorts pixels in the window and replaces the target pixel with the median value.
- RGB color images are processed separately for each color component (red, green, and blue).

## Implementation

- Two parallel filters are implemented using the Java Fork-Join framework.
- The window width (w) is a specified odd number >= 3.
- Median filter involves sorting, making it more computationally expensive than the mean filter.

## Usage

1. **Mean Filter:**
   ```java
   java ParallelMedianFilterApp mean input_image.jpg output_image.jpg w
   ```

2. **Median Filter:**
   ```java
   java ParallelMedianFilterApp median input_image.jpg output_image.jpg w
   ```

   - <img width="215" alt="Screenshot 2024-01-08 193542" src="https://github.com/Athi-sirmatt/Parallel_Programming-Image-Smoothing/assets/93771863/04a9dbb8-5697-49cf-a1c5-e0e7983bd593">  Input image.
   - <img width="203" alt="Screenshot 2024-01-08 193555" src="https://github.com/Athi-sirmatt/Parallel_Programming-Image-Smoothing/assets/93771863/0f07fbd7-5278-4838-a846-933e74f9a015">: Output image (Median Filter).
   - `w`: Window width (an odd number >= 3).

## Benchmarking

- Benchmark the parallel program to determine under which conditions parallelization is worthwhile.
- Compare the performance of the mean filter and the median filter under varying window widths.
- Analyze computational cost and parallelization benefits.

## How to Contribute

Contributions are welcome! Feel free to open issues or submit pull requests for enhancements or bug fixes.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments
- Image credits: [Source of original image]

---

**Note:** Replace placeholders like `input_image.jpg` and `output_image.jpg` with actual file paths. Provide proper credits for image sources. Adjust and expand the content based on actual project details and requirements.
