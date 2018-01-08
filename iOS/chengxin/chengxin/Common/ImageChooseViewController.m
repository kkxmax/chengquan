//
//  ImageChooseViewController.m
//  chengxin
//
//  Created by common on 3/9/16.
//  Copyright © 2016 chengxin. All rights reserved.
//

#import "ImageChooseViewController.h"
#import "UIView+Toast.h"
#import <AssetsLibrary/AssetsLibrary.h>
#import "TOCropViewController.h"
#import "GeneralUtil.h"

@interface ImageChooseViewController ()<TOCropViewControllerDelegate>

@property (nonatomic, strong) UIImage *image;           // The image we'll be cropping
@property (nonatomic, strong) UIImageView *imageView;   // The image view to present the cropped image

@property (nonatomic, assign) TOCropViewCroppingStyle croppingStyle; //The cropping style
@property (nonatomic, assign) CGRect croppedFrame;
@property (nonatomic, assign) NSInteger angle;

@end

@implementation ImageChooseViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.chooseView.layer.cornerRadius = 5;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onCamera:(id)sender
{
    if ([GeneralUtil isCameraAvailable] == NO) {
        [self dismissViewControllerAnimated:NO completion:nil];
        [self dismissViewControllerAnimated:NO completion:nil];
        
        UIAlertView *statusAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"这个应用需要访问您的相机。\n进入设置>诚乎>相机" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [statusAlert show];
    } else {
        UIImagePickerController* imagePicker = [[UIImagePickerController alloc] init];
        [imagePicker setDelegate:self];
        imagePicker.sourceType = UIImagePickerControllerSourceTypeCamera;
        imagePicker.editing = self.notEdit;
        [self presentViewController:imagePicker animated:YES completion:nil];
        self.view.hidden = YES;
    }
}
-(IBAction)onGallery:(id)sender
{
    if ([GeneralUtil isPhotoAvailable] == NO) {
        [self dismissViewControllerAnimated:NO completion:nil];
        [self dismissViewControllerAnimated:NO completion:nil];
        
        UIAlertView *statusAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"这个应用需要访问您的照片。\n进入设置>诚乎>照片" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [statusAlert show];
    } else {
        if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary]) {
            UIImagePickerController* imagePicker = [[UIImagePickerController alloc] init];
            [imagePicker setDelegate:self];
            imagePicker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
            
            [self presentViewController:imagePicker animated:YES completion:nil];
            self.view.hidden = YES;
        }
    }
}

#pragma mark - Cropper Delegate -
- (void)cropViewController:(TOCropViewController *)cropViewController didCropToImage:(UIImage *)image withRect:(CGRect)cropRect angle:(NSInteger)angle
{
    [self.delegate chooseViewController:self shownImage:image];
    [self dismissViewControllerAnimated:NO completion:nil];
}


#pragma mark - Image Picker Delegate -

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<NSString *,id> *)info
{
    if ([picker sourceType] == UIImagePickerControllerSourceTypePhotoLibrary) {
        if ([GeneralUtil isPhotoAvailable] == NO) {
            [self dismissViewControllerAnimated:NO completion:nil];
            [self dismissViewControllerAnimated:NO completion:nil];
            
            UIAlertView *statusAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"这个应用需要访问您的照片。\n进入设置>诚乎>照片" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [statusAlert show];
            return;
        }
    } else if ([picker sourceType] == UIImagePickerControllerSourceTypeCamera) {
        if ([GeneralUtil isCameraAvailable] == NO) {
            [self dismissViewControllerAnimated:NO completion:nil];
            [self dismissViewControllerAnimated:NO completion:nil];
            
            UIAlertView *statusAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"这个应用需要访问您的相机。\n进入设置>诚乎>相机" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [statusAlert show];
            return;
        }
    }

    [self dismissViewControllerAnimated:NO completion:nil];
    [self dismissViewControllerAnimated:NO completion:nil];
    NSURL *imageURL = (NSURL *)[info objectForKey:UIImagePickerControllerReferenceURL];
    ALAssetsLibrary *assetLibrary=[[ALAssetsLibrary alloc] init];
    [assetLibrary assetForURL:imageURL resultBlock:^(ALAsset *asset) {
        ALAssetRepresentation *rep = [asset defaultRepresentation];
        Byte *buffer = (Byte*)malloc(rep.size);
        NSUInteger buffered = [rep getBytes:buffer fromOffset:0.0 length:rep.size error:nil];
        NSData *imgData = [NSData dataWithBytesNoCopy:buffer length:buffered freeWhenDone:YES];//this is NSData may be what you want
        if(imgData.length > (1024 * 1024)) {
            [self.delegate chooseViewController:self shownImage:nil];
        }else{
            UIImage* image = [info objectForKey:UIImagePickerControllerOriginalImage];
            
            if (self.notEdit) {
                [self.delegate chooseViewController:self shownImage:image];
                return;
            }
            
            TOCropViewController *cropController = [[TOCropViewController alloc] initWithCroppingStyle:self.croppingStyle image:image];
            cropController.delegate = self;
            cropController.aspectRatioLockEnabled = YES;
            cropController.resetAspectRatioEnabled = NO;
            if (self.isSquareCrop) {
                cropController.aspectRatioPreset = TOCropViewControllerAspectRatioPresetSquare;
            } else {
                cropController.aspectRatioPreset = TOCropViewControllerAspectRatioPreset8x5;
            }
            [self presentViewController:cropController animated:NO completion:nil];
//            [self.delegate chooseViewController:self shownImage:[self scaleAndRotateImage:image]];
        }
    } failureBlock:^(NSError *err) {
        NSLog(@"Error: %@",[err localizedDescription]);
    }];

}

- (UIImage *)scaleAndRotateImage:(UIImage *)image {
    int kMaxResolution = 640; // Or whatever
    
    CGImageRef imgRef = image.CGImage;
    
    CGFloat width = CGImageGetWidth(imgRef);
    CGFloat height = CGImageGetHeight(imgRef);
    
    
    CGAffineTransform transform = CGAffineTransformIdentity;
    CGRect bounds = CGRectMake(0, 0, width, height);
    if (width > kMaxResolution || height > kMaxResolution) {
        CGFloat ratio = width/height;
        if (ratio > 1) {
            bounds.size.width = kMaxResolution;
            bounds.size.height = roundf(bounds.size.width / ratio);
        }
        else {
            bounds.size.height = kMaxResolution;
            bounds.size.width = roundf(bounds.size.height * ratio);
        }
    }
    
    CGFloat scaleRatio = bounds.size.width / width;
    CGSize imageSize = CGSizeMake(CGImageGetWidth(imgRef), CGImageGetHeight(imgRef));
    CGFloat boundHeight;
    UIImageOrientation orient = image.imageOrientation;
    switch(orient) {
            
        case UIImageOrientationUp: //EXIF = 1
            transform = CGAffineTransformIdentity;
            break;
            
        case UIImageOrientationUpMirrored: //EXIF = 2
            transform = CGAffineTransformMakeTranslation(imageSize.width, 0.0);
            transform = CGAffineTransformScale(transform, -1.0, 1.0);
            break;
            
        case UIImageOrientationDown: //EXIF = 3
            transform = CGAffineTransformMakeTranslation(imageSize.width, imageSize.height);
            transform = CGAffineTransformRotate(transform, M_PI);
            break;
            
        case UIImageOrientationDownMirrored: //EXIF = 4
            transform = CGAffineTransformMakeTranslation(0.0, imageSize.height);
            transform = CGAffineTransformScale(transform, 1.0, -1.0);
            break;
            
        case UIImageOrientationLeftMirrored: //EXIF = 5
            boundHeight = bounds.size.height;
            bounds.size.height = bounds.size.width;
            bounds.size.width = boundHeight;
            transform = CGAffineTransformMakeTranslation(imageSize.height, imageSize.width);
            transform = CGAffineTransformScale(transform, -1.0, 1.0);
            transform = CGAffineTransformRotate(transform, 3.0 * M_PI / 2.0);
            break;
            
        case UIImageOrientationLeft: //EXIF = 6
            boundHeight = bounds.size.height;
            bounds.size.height = bounds.size.width;
            bounds.size.width = boundHeight;
            transform = CGAffineTransformMakeTranslation(0.0, imageSize.width);
            transform = CGAffineTransformRotate(transform, 3.0 * M_PI / 2.0);
            break;
            
        case UIImageOrientationRightMirrored: //EXIF = 7
            boundHeight = bounds.size.height;
            bounds.size.height = bounds.size.width;
            bounds.size.width = boundHeight;
            transform = CGAffineTransformMakeScale(-1.0, 1.0);
            transform = CGAffineTransformRotate(transform, M_PI / 2.0);
            break;
            
        case UIImageOrientationRight: //EXIF = 8
            boundHeight = bounds.size.height;
            bounds.size.height = bounds.size.width;
            bounds.size.width = boundHeight;
            transform = CGAffineTransformMakeTranslation(imageSize.height, 0.0);
            transform = CGAffineTransformRotate(transform, M_PI / 2.0);
            break;
            
        default:
            [NSException raise:NSInternalInconsistencyException format:@"Invalid image orientation"];
            
    }
    
    UIGraphicsBeginImageContext(bounds.size);
    
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    if (orient == UIImageOrientationRight || orient == UIImageOrientationLeft) {
        CGContextScaleCTM(context, -scaleRatio, scaleRatio);
        CGContextTranslateCTM(context, -height, 0);
    }
    else {
        CGContextScaleCTM(context, scaleRatio, -scaleRatio);
        CGContextTranslateCTM(context, 0, -height);
    }
    
    CGContextConcatCTM(context, transform);
    
    CGContextDrawImage(UIGraphicsGetCurrentContext(), CGRectMake(0, 0, width, height), imgRef);
    UIImage *imageCopy = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return imageCopy;
}
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [self dismissViewControllerAnimated:NO completion:nil];
    [self dismissViewControllerAnimated:NO completion:nil];
}

-(IBAction)onCancel:(UITapGestureRecognizer*)recognizer
{
    [self dismissViewControllerAnimated:NO completion:nil];
}
@end
