//
//  HomeItemAddViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/1/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeItemAddViewController.h"
#import "ChoiceCategoryViewController.h"
#import "Global.h"
#import "SearchProductsViewController.h"
#import "UIImageView+WebCache.h"

@interface HomeItemAddViewController ()<ChoiceCategoryViewDelegate>
{
    NSArray *cityArray, *otherArray;
    ChoiceCategoryViewController *choiceCategoryVC;
    NSDictionary *selectedCategoryDic;
    NSString* categoryID;
    NSString *cityID, *cityOtherID;
    ImageChooseViewController *logoPicker;
    NSInteger cityRow, cityOtherRow;
    NSString *cityName;
    BOOL isPhotoSelected;
}
@end

@implementation HomeItemAddViewController
@synthesize cityPickerView, citySelectView;
- (void)viewDidLoad {
    [super viewDidLoad];
    isPhotoSelected = false;
    // Do any additional setup after loading the view from its nib.
    cityRow = 0;
    cityOtherRow = 0;
    cityName = @"";
    if([CommonData sharedInstance].addItemServiceIndex == ITEM_PAGE) {
        self.cityNoSelectView.hidden = YES;
        cityArray  = [[NSArray alloc] init];
        self.navTitleLabel.text = @"发布项目";
        [self getCityFromeServer];
    }else{
        self.cityNoSelectView.hidden = NO;
        self.navTitleLabel.text = @"发布服务";
        self.nameTitleLabel.text = @"服务名称：";
        self.categoryTitleLabel.text = @"服务分类：";
        self.reasonTitleLabel.text = @"地址：";
        self.contentTitleLabel.text = @"服务介绍：";
        self.networkTitleLabel.text = @"服务网址";
    }

    if(self.item != nil)
    {
        NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, self.item[@"logo"]]];
        [self.logoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
        isPhotoSelected = true;
        self.logoImageLabel.hidden = YES;
        
        if([self.item[@"isShow"] boolValue])
            self.isShowButton.selected = YES;
        else
            self.isShowButton.selected = NO;
        self.addressDetailTextField.text = self.item[@"addr"];
        self.cityLabel.text = [NSString stringWithFormat:@"%@, %@", self.item[@"provinceName"],self.item[@"cityName"]];
        cityName = self.cityLabel.text;
        cityID = self.item[@"provinceId"];
        cityOtherID = self.item[@"cityId"];
        self.needTextField.text = self.item[@"need"];
        self.categoryLabel.text = self.item[@"fenleiName"];
        categoryID = self.item[@"fenleiId"];
        self.nameTextField.text = self.item[@"name"];
        self.itemCommentTextView.text = self.item[@"comment"];
        self.weburlTextField.text = self.item[@"weburl"];
        self.contactNameTextField.text = self.item[@"contactName"];
        self.contactMobileTextField.text = self.item[@"contactMobile"];
        self.contactWeixinTextField.text = self.item[@"contactWeixin"];
    }
    self.itemCommentLimitLabel.text = [NSString stringWithFormat:@"%d/300", self.itemCommentTextView.text.length];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.itemCommentTextView scrollRectToVisible:CGRectMake(0, 0, 5, 5) animated:NO];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)getCityFromeServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getCityList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETCITYLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                cityArray = (NSMutableArray *)(dicRes[@"data"]);
                [self.cityPickerView reloadAllComponents];
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                                                 duration:3.0
                                                 position:CSToastPositionCenter
                                                    style:nil];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                                             duration:3.0
                                             position:CSToastPositionCenter
                                                style:nil];
        }
    }];
}

- (IBAction)onAdd:(id)sender {
    [self hideAllKeyboard];
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    NSString *actionName;
    if([CommonData sharedInstance].addItemServiceIndex == ITEM_PAGE) {
        actionName = @"addItem";
        [dicParams setObject:actionName forKey:@"pAct"];
        if(self.item != nil)
        {
            [dicParams setObject:self.item[@"id"] forKey:@"itemId"];
        }
        if(cityOtherID)
            [dicParams setObject:cityOtherID forKey:@"cityId"];
        else
        {
            [appDelegate.window makeToast:@"请选择所在城市"
                                 duration:3.0
                                 position:CSToastPositionCenter
                                    style:nil];
            return;
        }
        [dicParams setObject:self.addressDetailTextField.text forKey:@"addr"];
        [dicParams setObject:self.needTextField.text forKey:@"need"];
    }else{
        actionName = @"addService";
        [dicParams setObject:actionName forKey:@"pAct"];
        if(self.item != nil)
        {
            [dicParams setObject:self.item[@"id"] forKey:@"serviceId"];
        }
        [dicParams setObject:self.needTextField.text forKey:@"addr"];
    }

    
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    NSMutableDictionary *imageDictionary = [NSMutableDictionary dictionary];
    if(self.logoImageLabel.isHidden) {
        NSData *imageData = UIImagePNGRepresentation(self.logoImageView.image);
        [imageDictionary setObject:imageData forKey:@"logo"];
    }else
    {
        [GeneralUtil hideProgress];
        [appDelegate.window makeToast:@"应该上传一张图片"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    [dicParams setObject:self.nameTextField.text forKey:@"name"];
    if(selectedCategoryDic)
        [dicParams setObject:selectedCategoryDic[@"id"] forKey:@"fenleiId"];
    else if(categoryID != nil)
        [dicParams setObject:categoryID forKey:@"fenleiId"];
    else
        [dicParams setObject:@"" forKey:@"fenleiId"];
    [dicParams setObject:self.itemCommentTextView.text forKey:@"comment"];
    [dicParams setObject:self.weburlTextField.text forKey:@"weburl"];
    [dicParams setObject:self.isShowButton.selected? @"1" : @"0" forKey:@"isShow"];
    [dicParams setObject:self.contactNameTextField.text forKey:@"contactName"];
    [dicParams setObject:self.contactMobileTextField.text forKey:@"contactMobile"];
    [dicParams setObject:self.contactWeixinTextField.text forKey:@"contactWeixin"];
    
    [GeneralUtil showProgress];
    [[WebAPI sharedInstance] sendPostRequestWithUpload:actionName Parameters:dicParams UploadImages:imageDictionary :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                if(self.item == nil)
                    [CommonData sharedInstance].isPublished = true;
                if([[CommonData sharedInstance].lastClick isEqualToString:@"MainViewController"])
                {
                    SearchProductsViewController *searchProductsVC = [[SearchProductsViewController alloc] initWithNibName:@"SearchProductsViewController" bundle:nil];
                    if([CommonData sharedInstance].addItemServiceIndex == ITEM_PAGE) {
                        searchProductsVC.selected_tab = em_Item;
                    }else{
                        searchProductsVC.selected_tab = em_Service;
                    }
                    UINavigationController* navController = self.navigationController;
                    [self.navigationController popViewControllerAnimated:NO];
                    [navController pushViewController:searchProductsVC animated:YES];
                }else
                {
                    [self.navigationController popViewControllerAnimated:YES];
                }
                
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
    }];
}

- (void)showPickerView {
    if(citySelectView.isHidden) {
        [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        citySelectView.hidden = NO;
        [UIView animateWithDuration:1.0f animations:^{
            [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height - citySelectView.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        } completion:^(BOOL finished) {
        }];
    }else{
        [UIView animateWithDuration:1.0f animations:^{
            [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        } completion:^(BOOL finished) {
            citySelectView.hidden = YES;
        }];
    }
}
#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)showPickerAction:(id)sender {
    [self showPickerView];
}

- (IBAction)onCancelAction:(id)sender {
    [self showPickerView];
}

- (IBAction)onSelectCityAction:(id)sender {
    [self.view endEditing:YES];
    [self showPickerView];
    NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:cityRow];
    NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
    NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[cityOtherRow]);
    cityOtherID = cityOtherDic[@"id"];
    self.cityLabel.text = cityName;
}

- (IBAction)onChoiceKindAction:(id)sender {
    [self hideAllKeyboard];
//    if(!choiceCategoryVC) {
        choiceCategoryVC = [[ChoiceCategoryViewController alloc] initWithNibName:@"ChoiceCategoryViewController" bundle:nil];
        choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width, 64.f, self.view.frame.size.width - 44, self.view.frame.size.height - 64.f);
        choiceCategoryVC.delegate = self;
    if([CommonData sharedInstance].addItemServiceIndex == ITEM_PAGE) {
        choiceCategoryVC.titleLabel.text = @"项目分类：";
        choiceCategoryVC.categoryType = ITEM_CATEGORY;
    }else{
        choiceCategoryVC.titleLabel.text = @"服务分类：";
        choiceCategoryVC.categoryType = SERVICE_CATEGORY;
    }
        choiceCategoryVC.view.hidden = YES;
//    }
//    if(choiceCategoryVC.view.hidden) {
        choiceCategoryVC.selectedCategory = selectedCategoryDic;
        [choiceCategoryVC getData];
        choiceCategoryVC.view.hidden = NO;
        [self.view addSubview:choiceCategoryVC.view];
        [UIView animateWithDuration:1.0f animations:^{
            choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width - choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.origin.y, choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.size.height);
        } completion:^(BOOL finished) {
            self.transView.hidden = NO;
        }];
//    }
}

- (IBAction)onShowAction:(id)sender {
    [self hideAllKeyboard];
    self.isShowButton.selected = !self.isShowButton.selected;
}

- (IBAction)onSlideCategoryView:(UISwipeGestureRecognizer *)gestureRecognizer {
    [self.view endEditing:YES];
    [self hideChoiceCategoryView:nil];
}

-(IBAction)onLogo:(UITapGestureRecognizer*)recognizer {
    [self hideAllKeyboard];
    logoPicker = [[ImageChooseViewController alloc] initWithNibName:@"ImageChooseViewController" bundle:nil];
    logoPicker.delegate = self;
    logoPicker.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    logoPicker.isSquareCrop = YES;
    [self.navigationController presentViewController:logoPicker animated:YES completion:nil];
}


#pragma mark - ChoiceCategoryViewDelegate
- (void)hideChoiceCategoryView:(NSDictionary *)categoryDic {
    self.transView.hidden = YES;
    [UIView animateWithDuration:1.0f animations:^{
        choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width, choiceCategoryVC.view.frame.origin.y, choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.size.height);
    } completion:^(BOOL finished) {
        choiceCategoryVC.view.hidden = YES;
        [choiceCategoryVC.view removeFromSuperview];
    }];
    if(categoryDic) {
        selectedCategoryDic = categoryDic;
        self.categoryLabel.text = categoryDic[@"title"];
    }
}

#pragma mark - UIPickerViewDelegate, UIPickerViewDataSource

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 2;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if(component == 0) {
        return cityArray.count;
    }else{
        if(cityArray.count > 0) {
            NSDictionary *cityOtherDic = (NSDictionary *)[cityArray objectAtIndex:[pickerView selectedRowInComponent:0]];
            NSArray *cityOtherArray = (NSArray *)(cityOtherDic[@"cities"]);
            return cityOtherArray.count;
        }else{
            return 0;
        }
    }
}


- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    if(component == 0) {
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:row];
        return cityDic[@"name"];
    }
    else {
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:[pickerView selectedRowInComponent:0]];
        NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
        NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[row]);
        return cityOtherDic[@"name"];
    }
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if(component == 0) {
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:row];
        cityID = cityDic[@"id"];
        cityRow = row;
        NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
        NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[0]);
        cityOtherRow = 0;
        cityName = [NSString stringWithFormat:@"%@ , %@", cityDic[@"name"], cityOtherDic[@"name"]];
        [self.cityPickerView reloadAllComponents];
    }
    else {
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:[pickerView selectedRowInComponent:0]];
        NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
        NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[row]);
        cityOtherRow = row;
        cityName = [NSString stringWithFormat:@"%@ , %@", cityDic[@"name"], cityOtherDic[@"name"]];
    }
}

#pragma mark - UITextViewDelegate
- (void)textViewDidChange:(UITextView *)textView {
    NSInteger textLength = [self.itemCommentTextView.text length];
    if(textLength <= 300) {
        self.itemCommentLimitLabel.text = [NSString stringWithFormat:@"%d/300", (int)textLength];
    }
}
- (BOOL)textView:(UITextView *)iTextView shouldChangeTextInRange:(NSRange)iRange replacementText:(NSString *)iText {
    if (iRange.location < 300) {
        return YES;
    }else
        return NO;
    //iTextView.text = [iText substringToIndex:300];
}
#pragma mark - TextField Delegate
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    if(textField == self.nameTextField)
    {
        if(range.location >= 20)
            return NO;
        else
            return YES;
    }else if(textField == self.addressDetailTextField)
    {
        if(range.location >= 30)
            return NO;
        else
            return YES;
    }else if(textField == self.needTextField)
    {
        if(range.location >= 50)
            return NO;
        else
            return YES;
    }else if(textField == self.contactNameTextField)
    {
        if(range.location >= 6)
            return NO;
        else
            return YES;
    }else if(textField == self.contactMobileTextField)
    {
        if(range.location >= 11)
            return NO;
        else
            return YES;
    }else if(textField == self.contactWeixinTextField)
    {
        if(range.location >= 20)
            return NO;
        else
            return YES;
    }



    return YES;
}
#pragma mark - ImageChooseViewController
-(void)chooseViewController:(ImageChooseViewController *)vc shownImage:(UIImage *)image
{
    if(vc == logoPicker && image) {
        [self.logoImageView setImage:image];
        self.logoImageLabel.hidden = YES;
        isPhotoSelected = true;
    }
    else{
        [appDelegate.window makeToast:@"图片不能超过1M."
               duration:3.0
               position:CSToastPositionCenter
                  style:nil];
        self.logoImageLabel.hidden = NO;
    }
}

- (void)hideAllKeyboard {
    [self.view endEditing:YES];
    if(!citySelectView.isHidden) {
        [UIView animateWithDuration:1.0f animations:^{
            [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        } completion:^(BOOL finished) {
            citySelectView.hidden = YES;
        }];
    }
}

- (IBAction)handleTapCategoryView:(UITapGestureRecognizer *)recognizer {
    self.transView.hidden = YES;
    [UIView animateWithDuration:1.0f animations:^{
        choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width, choiceCategoryVC.view.frame.origin.y, choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.size.height);
    } completion:^(BOOL finished) {
        choiceCategoryVC.view.hidden = YES;
        [choiceCategoryVC.view removeFromSuperview];
    }];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
