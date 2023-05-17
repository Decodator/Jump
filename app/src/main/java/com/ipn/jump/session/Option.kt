package com.ipn.jump.session

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun Option(
    text: String,
    isSelected: Boolean = false,
    isRight: Boolean? = null,
    isCheckbox: Boolean = false,
    onSelected: () -> Unit,
    editable: Boolean = false,
    onEdit: (() -> Unit)? = null
){
    val color: Color = if (!isSelected && isRight == null) Color.Gray
    else if (isSelected && isRight == null) MaterialTheme.colorScheme.primary
    else if (isSelected && isRight == true) Color.Green
    else if (isSelected && isRight == false) Color.Red
    else Color.Gray

    val shape = if (!isCheckbox) RoundedCornerShape(100) else RoundedCornerShape(15)
    Surface(
        shape = shape,
        border = BorderStroke(width = 1.dp, color = color),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .selectable(
                    selected = isSelected,
                    onClick = onSelected,
                    role = Role.RadioButton
                )
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            if (editable){
                IconButton(onClick = onEdit!!) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Editar opción")
                }
            }
        }
    }
}