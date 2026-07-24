package xa.refile.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Material 3 shapes. The `large` radius (16dp) is tuned for poster-card
 * corners in the Infuse-style grid.
 */
val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraSmall = RoundedCornerShape(4.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
