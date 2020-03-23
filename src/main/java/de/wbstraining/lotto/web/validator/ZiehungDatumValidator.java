/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.validator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author gz1
 */
@FacesValidator(value="ziehungDatumValidator")
public class ZiehungDatumValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object obj) throws ValidatorException {
        Date date = (Date) obj;
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            String msg = ResourceBundle.getBundle("messages", locale).getString("errZiehungsdatum");
            FacesMessage message = new FacesMessage(msg);
            throw new ValidatorException(message);
        }
    }

}
